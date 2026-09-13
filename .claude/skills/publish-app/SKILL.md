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
is on one machine only and unbacked up. It points at `original-keystore.jks`, not
`upload-keystore.jks`.

**If `keystore.properties` is missing the release build still succeeds, unsigned**, because
`app/build.gradle` applies the signing config only when the file exists. Play rejects the
upload; the build gives no warning.

Play rejects an upload whose version code is not higher than the last one uploaded. Bump
`versionCode` in `app/build.gradle` (`versionName` is the user-visible string). Verify it
landed in the artifact, not just the config.

## Device

Faster than the Play track for iteration: no propagation wait, no tester opt-in. With more
than one device attached, every `adb shell` needs `-s <serial>` or it errors out.

```bash
adb -s $D install -r app/build/outputs/apk/release/app-release.apk
adb -s $D logcat -c                 # clear first, or you read the last boot
adb -s $D shell am start -n com.sujichim.jasanjao2/.MainActivity
adb -s $D logcat -d | grep -iE "FATAL|AndroidRuntime|com.sujichim.jasanjao2"
adb -s $D logcat -d | grep "queueBuffer: fps"     # sustained framerate
adb -s $D shell input tap <x> <y>
adb -s $D shell input swipe <x1> <y1> <x2> <y2> <ms>
adb -s $D exec-out screencap -p > /tmp/shot.png
```

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
`href`. Find elements by `innerText` and click them via JS, or locate by box model and
`click_at_xy`. Pin one tab and stay in it — do not navigate tabs the user is working in.

1. **Create release** — Test and release → Testing → Internal testing → Create new release.
2. **Upload the AAB.** The file input is hidden (`accept=".aab"`). Set it directly:
   ```python
   doc = cdp("DOM.getDocument", depth=-1, pierce=True)
   node = cdp("DOM.querySelector", nodeId=doc["root"]["nodeId"], selector="input[type=file]")
   cdp("DOM.setFileInputFiles", nodeId=node["nodeId"],
       files=["<abs path>/app/build/outputs/bundle/release/app-release.aab"])
   ```
   Processing takes ~40s. It is done when the release-name field auto-populates from the
   bundle.
3. **Release notes** live in one textarea with `<en-US>` language tags. State known gaps
   plainly — a tester hitting an unmentioned missing feature is worse than being told.
4. **Testers** — Testers tab → Create email list → name it, enter addresses, Enter to commit
   each, Save changes, then confirm the "available across all apps" dialog, then Save.
5. **Publish** — Preview and confirm → Save and publish → **then the same button again in a
   confirmation dialog**. That second click is the one that actually publishes and is easy
   to lose.

The publish landed when the track summary reads:

```
Active · Latest release: N (X.Y.Z) · Available to internal testers
```

**Draft** plus _"N of 3 complete"_ means it did not publish; the app list showing
`App status: Draft` confirms the same. The opt-in link only appears once a release is live —
before that the Testers tab says "the link will be shown here when you publish".

_No deobfuscation file_ is expected and not blocking: `minifyEnabled false`, so R8 never
runs. This is the Feb 2027 "app optimization below threshold" deadline.

There is no invitation email for internal testing; the opt-in link **is** the invitation. The
tester's Play Store app must be signed into the listed account — not just Chrome. Allow a few
minutes after accepting before the listing installs.

## Store listing

Name ≤30, short description ≤80, full description ≤4000, feature graphic 1024×500, ≥2 phone
screenshots. A privacy policy URL is mandatory and must be hosted somewhere you control.

The app ships AdMob (`play-services-ads`, `admobAppId` in `app/build.gradle`), so "Contains
ads" is Yes and the Data Safety form must declare what the ads SDK collects.
