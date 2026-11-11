# TapLink

**Your identity. One touch away.**

TapLink is a proximity-friendly digital identity product. The owner manages a profile in a native Android app; anyone can open the public profile in a browser without installing TapLink.

## Repository layout

- `android/` — native Android app (Kotlin + Jetpack Compose)
- `web/` — public profile website (next implementation phase)
- `supabase/` — database schema and security policies (next implementation phase)

## V1 approach

The first release is designed around a **physical NFC tag containing the owner's public profile URL**, with a QR code fallback in the Android app. This is the most reliable cross-platform path for visitors who do not have TapLink installed. Phone-to-phone NFC URL sharing is not assumed to work universally.

## Android app

Open the `android/` directory in Android Studio and allow Gradle sync. The app uses:

- Kotlin
- Jetpack Compose + Material 3
- Minimum Android API 26
- Compile/target SDK 36

Package: `app.taplink.android`

## Build

From the `android/` directory, run:

```bash
gradle assembleDebug
```

Or open the project in Android Studio and run the `app` configuration.

## Current milestone

This branch establishes the native app foundation and first-pass screen structure. Authentication, Supabase persistence, public profile hosting, NFC tag writing, and QR generation will be added in subsequent milestones. No production credentials are committed to this repository.
