---
name: publish-app
description: Build, sign, install and publish an Android app to Google Play. Covers producing a signed AAB/APK, testing the real build over adb on a device, and the Play Console flow through internal testing to production. Use when asked to ship, release, publish, upload a build, cut a version, put it on Play, test on device, or when an export or upload fails.
---

Four stages, each runnable on its own: **build → device → upload → production**.

The value here is the failure modes. Several of them are silent — a step reports success and
produces nothing, or a button stays disabled with no message. Those are marked.

## Per project

Read these off the repo before starting; every command below substitutes them.

| | |
| --- | --- |
| `$PKG` | application id, e.g. `com.example.thing` |
| `$DEV` | Play developer account id (the number in every console URL) |
| `$APP` | Play app id (the number after `/app/`) |
| `$TRACK` | internal-testing track id (the number after `/tracks/`) |
| `$AAB` / `$APK` | build outputs — paths differ by toolchain, see Build |
| listing language | `<en-US>`, `<ko-KR>`, … — read it off the console, do not assume |

Known accounts: `pkboom` (ppotpo@gmail.com), developer id `7021778550024261906`.

---

## Build

Goal: a **signed** AAB for Play, and optionally an APK for `adb install`. Play will not take
an APK; `adb` will not take an AAB.

### Gradle projects

```bash
./gradlew bundleRelease     # app/build/outputs/bundle/release/app-release.aab
./gradlew assembleRelease   # app/build/outputs/apk/release/app-release.apk
```

Signing usually reads a gitignored `keystore.properties` + `*.jks`. **If that file is missing
the release build still succeeds, unsigned** — the signing config is applied conditionally, so
there is no warning. Play rejects the upload later.

Check whether the keystore is the *upload* key or the **app signing key itself**. If a locally
built release installs over the Play build with no signature mismatch, it is the app signing
key: losing it means the app can never be updated again. An upload key, by contrast, is
recoverable — Play App Signing holds the real key and Google will reset an upload key.

### Godot projects

```bash
godot --headless --path . --export-release "<preset>" build/out.aab
```

Presets live in `export_presets.cfg` (usually gitignored — it holds the keystore path **and
password**). Keep two presets: AAB for Play, APK for adb. `gradle_build/export_format` is `1`
for AAB and `0` for APK, and **the output extension must match** or it fails after the whole
Gradle build has run, reading like a build error.

Four things must be true or export refuses, each with a different message: export templates
matching the engine version *exactly*; `textures/vram_compression/import_etc2_astc=true`; a
`config/icon`; and the extension above. A new image asset needs
`godot --headless --path . --import` before `preload()` can resolve it.

### Both toolchains

**A failed export/build can exit 0.** Never trust `$?`. Check the artifact:

```bash
ls -lh "$AAB"
jarsigner -verify "$AAB" | head -1        # want "jar verified."; unsigned says "jar is unsigned"
```

Play rejects a version code not higher than the last **uploaded** one — see the warning under
Upload, which is stricter than it sounds. Bump it, then verify it landed in the artifact, not
just in config:

```bash
aapt2 dump badging "$APK" | head -1       # versionCode / versionName
unzip -p "$AAB" base/manifest/AndroidManifest.xml | strings | grep -A1 versionName
```

`aapt2` cannot read an `.aab` (`could not identify format of APK`), and the AAB's
`BUNDLE-METADATA` holds plugin versions rather than the version code — so read the version off
an APK from the same build, or off the bundle manifest as above. `aapt2` lives in
`$ANDROID_HOME/build-tools/<latest>/`, not on `PATH`.

Also confirm the bundle actually contains what you just added, e.g.
`unzip -l "$AAB" | grep -c sfx`.

---

## Device

Faster than the Play track: no propagation wait, no tester opt-in. Every `adb` call needs
`-s $D` whenever an emulator is also attached.

```bash
adb devices -l
adb -s $D install -r "$APK"
adb -s $D shell dumpsys package $PKG | grep -E "versionCode|versionName"
adb -s $D logcat -c                       # clear first, or you read the last boot
adb -s $D shell monkey -p $PKG -c android.intent.category.LAUNCHER 1
adb -s $D shell dumpsys window | grep mCurrentFocus
adb -s $D logcat -d | grep -iE "FATAL|AndroidRuntime|SCRIPT ERROR|$PKG"
adb -s $D logcat -d | grep "queueBuffer: fps"     # sustained framerate
adb -s $D shell input tap <x> <y>
adb -s $D shell input swipe <x1> <y1> <x2> <y2> <ms>
adb -s $D exec-out screencap -p > /tmp/shot.png
adb -s $D shell pm clear $PKG             # wipe saved state for a true first-run test
```

Launch with `monkey`, not `am start -n .../<Activity>` — the latter silently bounces to the
launcher activity for anything not exported, which looks like a clean launch of the wrong
screen. `mCurrentFocus` is the cheap navigation assertion: tap, wait ~4s, read it back.

**Check the device is yours to drive.** If the user is holding the phone, taps land in
whatever app they have open and every reading is meaningless. `mCurrentFocus` before and after
is the check.

A backgrounded emulator throttles: clicks register while rendering stalls for seconds. Bring
it forward before concluding nothing happened.

Most `E/` lines on a Samsung are system noise — `WifiVendorHal`, `RestrictModeUtil`,
`PowerUI`, battery `audit`.

**Measure** the PNG rather than eyeballing it — sample pixel spans and compare against where
the gesture aimed.

Synthetic input is not proof for gestures. It exercises the same routing, but confirm with a
real finger before calling a gesture fixed or broken.

A save feature is not tested until the process has died (`am force-stop`, confirm with
`pidof`, relaunch).

Samsung's blue light filter is captured by `screencap`, so check before reporting a rendering
bug or using device shots as store assets:

```bash
adb -s $D shell settings list system | grep -iE "blue_light|night_mode"
```

`blue_light_filter=1` suppresses blue and boosts red across every colour, worst in darks. It
is the phone, not the renderer.

---

## Upload

Internal testing needs no Google review and no content declarations.

### Driving the console

The Play Console is a JS app: the accessibility tree is sparse and many controls have no
`href`. Find elements by `innerText` and click via JS, or by box model and `click_at_xy`.

**Open your own tab and never drive one the user has open.** Each `browser-harness` process
re-attaches to Chrome's *active* tab, so `switch_tab(TAB)` at the top of every invocation and
again before each click. Look up your tab by a stored `targetId`, not by URL matching — URL
matching adopts the user's tab when yours is gone, and that is how a click lands on the wrong
app. Guard every evaluation:

```python
js("(() => { if(!location.href.includes(APP)) return 'WRONG APP'; return (…)(); })()")
```

Pages render slowly and sometimes not at all — 40s is normal, and a body stuck at a few
hundred characters for two minutes means it will not render; reload or try a different route.
Hard-loading a client-side route (`…/app-settings`) can render nothing where navigating to it
in-app works.

### Steps

1. **Create release** — Test and release → Testing → Internal testing → Create new release.
2. **Upload the bundle.** The file input is hidden (`accept=".aab"`):
   ```python
   doc = cdp("DOM.getDocument", depth=-1, pierce=True)
   node = cdp("DOM.querySelector", nodeId=doc["root"]["nodeId"], selector="input[type=file]")
   cdp("DOM.setFileInputFiles", nodeId=node["nodeId"], files=[AAB])
   ```
   Processing takes ~40s. The release-name field **sometimes** auto-populates from the bundle
   and sometimes does not, so poll for the `App bundles` section instead.

   **Uploading burns the version code immediately, even if the release is never saved.** Play
   registers the bundle against the app on upload. Rebuild with the same code and every retry
   is rejected — and *"Version code N has already been used"* surfaces at the **review** step,
   long after the upload looked fine. Bump and rebuild.

   Failed uploads leave `error`-marked rows stacked in the upload area; each has a `clear`
   control and all must go or the error persists. A good row has `delete`, not `clear`.

   **Nothing persists until a save lands.** Reload and confirm the bundle is still attached —
   a release will happily save its name and notes while silently dropping the bundle.
3. **Release name is required**, and its absence is the usual reason Save is disabled with no
   visible error. Type `N (X.Y.Z)` if it did not auto-populate.
4. **Release notes**, one textarea tagged with the listing's languages — read the placeholder
   rather than assuming the language. **Capped at 500 characters per language**; over that the
   only symptom is a permanently disabled Save, with *"Release note for &lt;lang&gt; is too
   long"* sitting next to the field.

   State known gaps plainly — a tester hitting an unmentioned missing feature is worse than
   being told.

### Writing into the console's fields

Two mechanisms, and the wrong one fails silently.

On **store listing** fields the framework setter works:

```python
js("""(() => { const el=document.querySelector(SEL);
  const proto = el.tagName==='TEXTAREA' ? HTMLTextAreaElement : HTMLInputElement;
  Object.getOwnPropertyDescriptor(proto.prototype,'value').set.call(el, V);
  el.dispatchEvent(new Event('input',{bubbles:true}));
  el.dispatchEvent(new Event('change',{bubbles:true})); })()""")
```

On **`releases/N/prepare`** it is not enough: the DOM updates but the form stays pristine, so
Save stays disabled and the value is discarded on reload. That page needs real key events:

```python
js("(() => { const t=document.querySelector('textarea'); t.scrollIntoView({block:'center'}); t.focus(); t.select(); })()")
press_key("Backspace"); type_text(notes)
```

Use `element.focus()`, **not** `click_at_xy` — a coordinate click on these fields lands on
`BODY` (verify with `document.activeElement`) and types nothing. Always read `.value` back.

### Assets are a library, not a slot

Uploading an image does not fill the slot it was uploaded from. It lands in a shared asset
library panel; a separate **Add** control (its `innerText` contains the icon ligature, e.g.
`add_photo_alternate`) assigns the selected asset to the slot. Verify the slot shows `N / M`
afterwards. *"Asset was deduplicated"* just means Play recognised the file hash.

### Testers

Testers tab → Create email list → name it, enter addresses, Enter to commit each, Save
changes, confirm the "available across all apps" dialog, Save. Lists are account-wide but are
**not** attached to a track automatically. Whose addresses go on one is the user's call — ask.

There is no invitation email for internal testing; the opt-in link **is** the invitation, and
appears only once a release is live. The tester's Play Store app must be signed into the
listed account — not just Chrome. Allow a few minutes after accepting before it installs.

### Publish

Preview and confirm → Save and publish → a dialog reading *"Publish change on Google Play?"*
with its **own** Save and publish button. **Click that in a separate round-trip, after
confirming the dialog rendered** — firing both in one pass finds only the page's button and
silently does nothing. Check that two buttons with that label exist, then click the last.

Verify afterwards from the track summary, not from the click:

- `Active · Latest release: N (X.Y.Z)` — published.
- **Draft** plus *"N of 3 complete"* is the real failure signal, matching `App status: Draft`
  in the app list.
- `Inactive` does **not** mean failure — a published release sits there until a tester list is
  attached.

On the review page, `Size for new installs` showing a real figure proves a bundle is attached;
*"New app bundles will be shown here"* means it is not.

Two warnings are expected on a clean release and neither blocks: *no deobfuscation file* (R8
never ran — relevant to the Feb 2027 "app optimization below threshold" deadline) and *no
native debug symbols*. The Preview step also diffs device support: `0` under "Devices no
longer supported" confirms a `minSdk`/manifest change dropped no hardware.

---

## Production

Internal testing needs none of this; production needs all of it, plus a Google review that the
testing tracks skip. Expect days for a first submission.

- **Store listing** — name ≤30, short description ≤80, full description ≤4000, app icon
  512×512, feature graphic 1024×500, ≥2 phone screenshots, and **7-inch and 10-inch tablet
  screenshots** (each 320–3840 px, longest side under twice the shortest).
- **Privacy policy URL** — mandatory, publicly reachable with no login, and it must stay live;
  Play re-checks and a dead URL can get an app suspended later. GitHub Pages on a public repo
  works and is free.
- **App content** — around ten policy declarations. Each is an attestation, not data entry.
- **Data Safety** must agree with the privacy policy text; Play cross-checks them. If the app
  embeds an ads SDK, declare what that SDK collects — advertising ID, device identifiers,
  IP-derived approximate location, app interactions, diagnostics — as both *collected* and
  *shared*. A third-party SDK's collection counts as yours.
- **Contains ads** — Yes if any ads ship.
- **Target audience** changes what else applies: declaring 13+ avoids the Families programme;
  any under-13 audience imposes ad-content restrictions. This is the user's decision.

Until the app is reviewed, testers see the package name plus `(unreviewed)` rather than the
app name.

### Ads

Ship **test** ad unit ids in debug and real ones only in release. Tapping your own live ad is
invalid traffic and can cost the account; a test id shipped to production merely earns
nothing. Ad units are per-app and cannot be shared between apps.

Interstitials must not appear on launch or during play — only at a natural break. For a
short-session game, one per session is the frequency Google enforces against; gate on both a
count and a wall-clock floor.
