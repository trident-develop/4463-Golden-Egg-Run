# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Color Flow — Android puzzle game (Kotlin + Jetpack Compose). Package: `com.pinger.textf`. Target API 36, minSdk 28. Portrait-only.

## Build Commands

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK
./gradlew test                   # Run unit tests
./gradlew connectedAndroidTest   # Run instrumented tests
./gradlew testDebugUnitTest --tests "com.pinger.textf.ExampleUnitTest"  # Single test
```

## Architecture

- **Single module** (`app`) with Gradle Kotlin DSL + version catalog (`gradle/libs.versions.toml`)
- **UI**: Jetpack Compose + Material3, edge-to-edge, Navigation Compose
- **Activities**: `LoadingActivity` (launcher, 2s splash) -> `MainActivity` (hosts all screens via NavHost)
- **Navigation routes**: menu, leaderboard, settings, how_to_play, privacy_policy, levels, game/{levelId}

## Code Structure

- `components/` — Reusable composables (MenuButton, SquareButton, GameBackground, ScreenTitle, press modifiers)
- `screens/` — Screen composables (Menu, Levels, Game, Leaderboard, Settings, HowToPlay, PrivacyPolicy, Loading)
- `model/` — Data classes (Level, ColorPair, GameState)
- `data/` — LevelProvider with 40 hand-crafted levels (4x4 -> 7x7)
- `storage/` — GamePreferences (SharedPreferences wrapper for progress, times, settings)
- `audio/` — MusicManager (MediaPlayer), VibrationHelper
- `navigation/` — NavRoutes + AppNavGraph
- `ui/theme/` — GameFont (custom font from res/font/font.ttf), colors, Material3 theme

## Game Logic

Color Flow puzzle: connect same-color dot pairs on a grid via orthogonal paths without crossing. GameState tracks paths per color. Touch handling via detectDragGestures in Canvas pointerInput. Level completion checked by `GameState.isAllConnected()`.

## Key Conventions

- All buttons use `pressableWithCooldown` modifier for press animation + cooldown
- `SquareButton` for icon buttons (back, pause, replay); `MenuButton` for text buttons with button_blue.png background
- `GameFont` (from res/font/font.ttf) used for all game text
- `GameBackground` wraps all screens with bg_1.png fullscreen background
