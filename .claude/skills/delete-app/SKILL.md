---
name: delete-app
description: Delete apps from the Google Play Console via browser-harness.
argument-hint: "[<app or package names to delete>]"
---

# delete-app

There is no API for this — it is browser work through `browser-harness`.

## Before deleting anything

1. Enumerate **every** app at `https://play.google.com/console/u/0/developers/<devId>/app-list`. The list paginates at 10.
2. Confirm the delete list with the user by **package name**, with install counts and app status. Display names collide (two apps can both be `자오유주2`); the package name is the identity.
3. Collect each app's internal console id — deletion is per-app-id.

```python
# app rows are divs, not <a>; the per-row "View <name>" link carries the id
nodes = cdp("Accessibility.getFullAXTree")["nodes"]
for n in nodes:
    nm = (n.get("name") or {}).get("value", "") or ""
    if (n.get("role") or {}).get("value") == "link" and nm.startswith("View "):
        o = cdp("DOM.resolveNode", backendNodeId=n["backendDOMNodeId"])["object"]["objectId"]
        href = cdp("Runtime.callFunctionOn", objectId=o, returnByValue=True,
                   functionDeclaration="function(){return this.getAttribute('href')}")["result"]["value"]
        print(nm, href)   # .../app/<appId>/app-dashboard
```

## Eligibility — check first, it explains most failures

Self-service deletion requires all of:

1. Unpublished, with no changes in review
2. Fewer than 1,000 active installs
3. Not rejected, blocked, or suspended

Monetized apps (paid, IAP, subscriptions) are ineligible unless lifetime installs are zero. Only account admins can delete.

**A "Removed by Google" app can never be deleted.** The enforcement flag is permanent and unpublishing does not clear it — the disqualifier is the unresolved enforcement action, not the publish state. The page shows `This app is not eligible for deletion` with the button disabled. Only an appeal or a compliant release clears it; for an old takedown both are effectively dead. Tell the user instead of retrying.

**A still-Published app shows no delete section at all** — just the note _"Some apps can be deleted once they're unpublished."_ Unpublish first (radio → `Unpublish` → confirm in the dialog), then the section appears. Ask before unpublishing: it is a separate outward-facing change, wasted if the app turns out ineligible.

## The transaction ID gate

Every deletion demands the developer-account **registration fee transaction ID**, from `https://pay.google.com/gp/w/u/0/home/activity` → the `Google Play Developer Token` ($25) transaction.

That page renders in a cross-origin iframe, so `document.body.innerText` on the top frame returns nothing. Read it from the frame:

```python
fid = [f["frame"]["id"] for f in cdp("Page.getFrameTree")["frameTree"]["childFrames"]
       if "timelineview" in f["frame"]["url"]][0]
ctx = cdp("Page.createIsolatedWorld", frameId=fid, worldName="probe")["executionContextId"]
print(cdp("Runtime.evaluate", expression="document.body.innerText",
          contextId=ctx, returnByValue=True)["result"]["value"])
```

**Format trap:** strip the leading segment or Play Console answers `Enter a valid transaction ID`. Accepted shapes are `token.<digits>`, `0.G.<digits>`, `Registration-<uuid>`, `PDS.<digits>` — so `05912476870148976978.token.2015264274772589` must be entered as `token.2015264274772589`.

## The delete flow

Path: `.../app/<appId>/advanced-distribution` (the "Advanced settings" page; `advanced-settings` is not a real route and redirects to the app list).

1. Click the page-level **Delete app**.
2. Dialog 1 — _Enter transaction ID and package name_: fill both, click **Next**. A wrong transaction ID bounces back to this step with the field marked red.
3. Dialog 2 — _Finish deleting your app_: type `delete`, click **Delete app**.
4. Success shows a red banner: `You have until <date> to recover this deleted app`.

Deletion is reversible for **7 days** via `Recover app` on the same page, then permanent. Package names are never reusable afterwards. Say both when reporting.

## Play Console automation pitfalls

**The modal root is `material-dialog`.** It is portalled into `.acx-overlay-container`, so the dialog's text is absent from `document.body.innerText` and `.popup-wrapper` is a different, unrelated overlay. Scope every dialog query to `document.querySelector('material-dialog')`.

**Backgrounded tabs are throttled.** Clicks register but Angular takes many seconds to render the dialog, or the app list never populates. Call `activate_tab(TAB)` before driving the page; it steals focus from the user.

**The marker drifts between processes.** Each `browser-harness` invocation re-attaches to Chrome's _active_ tab, so if the user is browsing, your next call runs against their tab. Pin the tab and assert the URL before every click:

```python
def guard():
    switch_tab(TAB)
    u = page_info()["url"]
    if expected_app_id not in u:
        raise RuntimeError("wrong page: %s" % u)
```

Abort rather than click when the assertion fails — this is what stops a destructive click landing on the wrong app. Poll until `goto_url` has landed before asserting; navigation lags the call.

**Smooth scrolling makes coordinates stale.** `scrollIntoView` animates, so a box model read straight after it returns pre-scroll coordinates and the click misses silently. Read a live rect and hit-test it, retrying a few times — the page settles slowly:

```python
def btn_xy(label, scope="null"):
    r = js("""(function(){
      var root = %s || document;
      var b = Array.from(root.querySelectorAll('button')).find(
        x => x.textContent.trim() === %s && !x.disabled && x.offsetParent !== null);
      if (!b) return '';
      b.scrollIntoView({block:'center'});
      var q = b.getBoundingClientRect(), cx = q.left+q.width/2, cy = q.top+q.height/2;
      var e = document.elementFromPoint(cx, cy);
      return JSON.stringify([cx, cy, !!(e && (e===b || b.contains(e) || e.closest('button')===b))]);
    })()""" % (scope, json.dumps(label)))
    if not r: return None
    cx, cy, ok = json.loads(r)
    return (cx, cy) if ok else None
```

**Text inputs need select-then-type.** `press_key("Meta+a")` does not select; typing then appends to the existing value and corrupts the field. Use `i.focus(); i.select()` via `js(...)`, then `type_text(...)`, then read `.value` back to verify before submitting.

**Rendering is genuinely flaky.** Retry each app 3 times, and treat `ineligible` as a terminal result, not a failure to retry.
