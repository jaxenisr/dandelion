# Dandelion

Dandelion is a frontend for a Nintendo 3DS emulator on Android. It is the shell around the
emulator: a library of games, a game details page, the emulation screen with its controls and
the emulator settings. The interface is built by hand in Kotlin and Jetpack Compose to look and
behave like a SwiftUI app on iOS, not like an Android app.

There is **no emulator core yet**. The emulation screen shows static placeholder gameplay that
the app draws itself, and the settings are stored but not acted on. Nothing in the app pretends
to run a game. Wiring up a core such as Azahar or libretro is the next step and is described at
the bottom of this file.

## Screens

- **Games** – the library, two columns of covers with name and size. Tapping the title opens the
  glass menu with Games, Emulation and Settings.
- **Emulation** – black, with the 16:9 top screen, a progress panel with Continue, the circle pad,
  the ABXY buttons and Start · Select.
- **Settings** – Core and Graphics sections with toggles, stepped values and the Old/New switch.
- **Game details** – cover, title, meta line, Play button and the save state, cheats, region and
  language rows.

Navigation is a small stack in `DandelionViewModel`; there is no navigation library. Settings are
persisted with DataStore. The sample library lives in `data/Game.kt` and every cover is a gradient
drawn at runtime from that game's palette, so no cover art ships with the app.

## Building

Requirements: JDK 17 and an Android SDK with platform 35 and build-tools 35.0.0. Point Gradle at
the SDK with `ANDROID_HOME` or a `local.properties` file containing `sdk.dir=<path to the SDK>`.

```
./gradlew assembleDebug
```

The APK is written to:

```
app/build/outputs/apk/debug/app-debug.apk
```

Install it on a connected phone with:

```
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Minimum Android version is 8.0 (API 26), target and compile SDK are 35.

## Design

The four screens follow the files in `design/`. Tokens are in `ui/Tokens.kt`: white background,
black text, `#86868B` secondary text, `#E3E3E7` hairlines, `#007AFF` blue, `#34C759` green and
`#6E56E2` purple. The type scale uses Inter (Regular, Medium, SemiBold, Bold) shipped in
`res/font`, with a 33 sp bold large title and 13 sp grey subtitles.

Nothing comes from Material Design. There is no `MaterialTheme`, no `Scaffold`, no Material
button, switch, card or ripple, and no elevation shadow except the soft one under the glass menu.
`Text` is `BasicText` from Foundation, every surface is a `Box` with a background and a corner
radius, and all icons are drawn with `Canvas` in `ui/components/Icons.kt`. Taps dim the surface to
55% instead of showing a ripple.

The title menu is a translucent white surface with a 0.5 dp border and a soft shadow. On Android
12 and newer the page behind it is drawn a second time and blurred with `Modifier.blur`, clipped
to the menu shape; on older versions the translucency alone carries the effect.

## What is missing

- **The emulator core.** No core is bundled, no ROM is loaded and no frame is emulated. The
  emulation screen is a static placeholder: the top screen is a gradient landscape, the progress
  panel is sample data and the controls do not drive anything.
- **Game importing.** The `+` button is the place for it, but there is no file picker and no ROM
  scanning yet; the library is the four sample entries in `data/Game.kt`.
- **Real covers.** Covers are gradients with the game title. A real library would load artwork
  from the file system.
- **Settings plumbing.** The settings are persisted, but a core would have to read them.

To add a core, the natural seam is `DandelionViewModel.play()` and the `EmulationScreen`: replace
the placeholder artwork with a surface fed by the core, and feed the pad and buttons into its input
state. The JNI bindings, the ROM picker and the frame surface all sit behind that seam; nothing in
the current UI has to change for them.

## License

Apache 2.0, see `LICENSE`.
