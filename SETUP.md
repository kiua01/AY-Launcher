# Setup notes

This is a Compose Multiplatform project skeleton.

To make it fully runnable you still need:

1. Generate Gradle Wrapper:
   ```
   gradle wrapper --gradle-version 8.10.2
   ```
   (or open in Android Studio / IntelliJ which can generate it)

2. Sync the project in Android Studio / IntelliJ IDEA with the Compose Multiplatform plugin.

3. For Desktop run: `./gradlew :desktopApp:run`

4. For Android: create an emulator or use a device with large screen (sw ≥ 1000 recommended). Install via Android Studio.

5. Rust core is independent for now. Later we will add UniFFI bindings to call from Kotlin.

The UI is intentionally a high-fidelity visual match to the provided screenshots (cream theme, yellow CTAs, Nether banners, card layouts, sidebar).

Next development steps are listed in the main README.
