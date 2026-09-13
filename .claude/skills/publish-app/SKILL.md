---
name: publish-app
description: Build, sign, install and publish app to Google Play.
---

Developer account `pkboom` (ppotpo@gmail.com), ID `7021778550024261906`.

## Build

```bash
./gradlew bundleRelease     # app/build/outputs/bundle/release/app-release.aab  → Play
./gradlew assembleRelease   # app/build/outputs/apk/release/app-release.apk     → adb install
```

Signing reads `keystore.properties` at the repo root — gitignored along with `*.jks`, so it
is on one machine only and unbacked up. It points at `original-keystore.jks` (alias `spike`),
which is the **app signing key itself**, not an upload key: a local release APK installs over
the Play build with no signature mismatch. Lose it and the app can never be updated again.

**If `keystore.properties` is missing the release build still succeeds, unsigned**, because
`app/build.gradle` applies the signing config only when the file exists. Play rejects the
upload; the build gives no warning.

Play rejects a version code not higher than the last uploaded. Bump `versionCode` in
`app/build.gradle` (`versionName` is the user-visible string), then verify the artifact:

```bash
aapt2 dump badging app/build/outputs/apk/release/app-release.apk | head -1
jarsigner -verify app/build/outputs/bundle/release/app-release.aab | head -1  # "jar verified."
```

`aapt2` cannot read an `.aab` (`could not identify format of APK`) and the AAB's
`BUNDLE-METADATA` holds plugin versions, not the version code — so check the version on the
APK from the same build. `aapt2` is at `$ANDROID_HOME/build-tools/<latest>/`, not on `PATH`.
An unsigned bundle reports "jar is unsigned" and has no `META-INF/*.RSA`.

## Device

Faster than the Play track: no propagation wait, no tester opt-in. Every `adb` call needs
`-s <serial>` whenever an emulator is also attached.

```bash
adb -s $D install -r app/build/outputs/apk/release/app-release.apk
adb -s $D shell dumpsys package com.sujichim.jasanjao2 | grep -E "versionCode|versionName"
adb -s $D logcat -c                 # clear first, or you read the last boot
adb -s $D shell monkey -p com.sujichim.jasanjao2 -c android.intent.category.LAUNCHER 1
adb -s $D shell dumpsys window | grep mCurrentFocus
adb -s $D logcat -d | grep -iE "FATAL|AndroidRuntime|com.sujichim.jasanjao2"
adb -s $D logcat -d | grep "queueBuffer: fps"     # sustained framerate
adb -s $D shell input tap <x> <y>
adb -s $D shell input swipe <x1> <y1> <x2> <y2> <ms>
adb -s $D exec-out screencap -p > /tmp/shot.png
```

Launch with `monkey`, not `am start -n .../<Activity>` — the latter silently bounces to the
launcher activity for anything not exported, which looks like a clean launch of the wrong
screen. `mCurrentFocus` is the cheap navigation assertion: tap, wait ~4s, read it back.

A backgrounded emulator throttles: clicks register while rendering stalls for seconds. Bring
it forward before concluding nothing happened.

Most `E/` lines on a Samsung are system noise — `WifiVendorHal`, `RestrictModeUtil`,
`PowerUI`, battery `audit`.

**Measure** the PNG rather than eyeballing it — sample pixel spans and compare against where
the gesture aimed.

Synthetic input is not proof for gestures. It exercises the same routing, but confirm with a
real finger before calling a gesture fixed or broken.

A save feature is not tested until the process has died.

Samsung's blue light filter is captured by `screencap`, so check before reporting a rendering
bug or using device shots as store assets:

```bash
adb -s $D shell settings list system | grep -iE "blue_light|night_mode"
```

`blue_light_filter=1` suppresses blue and boosts red across every colour, worst in darks. It
is the phone, not the renderer.

## Upload

Internal testing needs no Google review and no content declarations.

The Play Console is a JS app; the accessibility tree is sparse and many controls have no
`href`. Find elements by `innerText` and click them via JS, or by box model and `click_at_xy`.
Open your own tab with `new_tab(...)` and keep its `targetId` — never drive a Play Console tab
the user has open. Each `browser-harness` process re-attaches to Chrome's *active* tab, so
`switch_tab(TAB)` at the top of every invocation and again before each click. A pin from an
earlier turn may be gone (`No target with given id found`); open a fresh tab.

1. **Create release** — Test and release → Testing → Internal testing → Create new release.
2. **Upload the AAB.** The file input is hidden (`accept=".aab"`). Set it directly:
   ```python
   doc = cdp("DOM.getDocument", depth=-1, pierce=True)
   node = cdp("DOM.querySelector", nodeId=doc["root"]["nodeId"], selector="input[type=file]")
   cdp("DOM.setFileInputFiles", nodeId=node["nodeId"],
       files=["<abs path>/app/build/outputs/bundle/release/app-release.aab"])
   ```
   Processing takes ~40s, done when the release-name field auto-populates from the bundle.
3. **Release notes** — one textarea tagged with the listing's languages (`<ko-KR>` here, not
   `<en-US>`); read the placeholder instead of assuming. The field is framework-controlled,
   so a plain `.value` assignment is discarded:
   ```python
   js("""(function(){
     var t = document.querySelector('textarea');
     var set = Object.getOwnPropertyDescriptor(HTMLTextAreaElement.prototype,'value').set;
     set.call(t, %s);
     t.dispatchEvent(new Event('input',{bubbles:true}));
     t.dispatchEvent(new Event('change',{bubbles:true}));
   })()""" % json.dumps(notes))
   ```
   Read `.value` back. State known gaps plainly — a tester hitting an unmentioned missing
   feature is worse than being told.
4. **Testers** — Testers tab → Create email list → name it, enter addresses, Enter to commit
   each, Save changes, confirm the "available across all apps" dialog, Save. Lists are
   account-wide but are **not** attached to a track automatically. Whose addresses go on one
   is the user's call — ask.
5. **Publish** — Preview and confirm → Save and publish → a `material-dialog` reading *"Publish
   change on Google Play?"* with its own Save and publish button. Scoping the lookup to that
   dialog found no `<button>` inside it, yet the release still went live — trust neither the
   click nor its absence. Verify, and re-click only if it did not land.

Verify by the release row, which reads `N (X.Y.Z) · Available to internal testers · Released
on <date>`.

**`Inactive` in the track summary does not mean failure** — a published release sits under
`Inactive` until a tester list is attached. **Draft** plus _"N of 3 complete"_ is the real
failure signal, matching `App status: Draft` in the app list.

Two warnings appear on a clean release: *no deobfuscation file* (expected — `minifyEnabled
false`, so R8 never runs; the Feb 2027 "app optimization below threshold" deadline) and *no
testers specified*. Neither blocks. The Preview step also diffs device support: `0` under
"Devices no longer supported" confirms a `minSdk`/manifest change dropped no hardware.

There is no invitation email for internal testing; the opt-in link **is** the invitation, and
appears only once a release is live. The tester's Play Store app must be signed into the
listed account — not just Chrome. Allow a few minutes after accepting before it installs.

## Store listing

Name ≤30, short description ≤80, full description ≤4000, feature graphic 1024×500, ≥2 phone
screenshots. A privacy policy URL is mandatory and must be hosted somewhere you control.

The app ships AdMob (`play-services-ads`, `admobAppId` in `app/build.gradle`), so "Contains
ads" is Yes and the Data Safety form must declare what the ads SDK collects.
