# AY Launcher

**AY Launcher** — A modern, beautiful Minecraft: Java Edition launcher for **PC (Desktop)** and **Android** (designed for devices with smallest width ≥ 1000 dp).

Built **from scratch** using:
- **Kotlin** + **Compose Multiplatform** for the UI (shared across Android & Desktop)
- **Rust** for performance-critical core (version management, downloads, process launching helpers)
- **Java** interop where needed for Minecraft/Java runtime integration

> This project recreates a polished launcher experience inspired by the modern "O Launcher Plus / CS Launcher Plus" style UI (Nether-themed home, instance creation, file manager, account selection, settings), but implemented cleanly from the ground up.

## Goals

- 100% visual match to the provided design language (rounded cards, soft cream/beige backgrounds, vibrant Nether banners, yellow accents, clean icons)
- Support Offline + Microsoft accounts
- Instance management (Vanilla / Fabric / Forge / NeoForge / Quilt / OptiFine)
- Built-in File Manager for `.minecraft`, mods, resource packs, shaderpacks, screenshots, etc.
- Modpack browsing & installation flow
- Desktop (Windows / Linux / macOS via Compose Desktop) + Android (large-screen optimized)
- Clean architecture, modular, maintainable

## Project Structure

```
AY-Launcher/
├── README.md
├── LICENSE
├── docs/
│   └── assets/          # Reference screenshots of the target UI
├── shared/              # Kotlin Multiplatform shared code (UI + domain)
│   └── src/
│       ├── commonMain/
│       ├── androidMain/
│       └── desktopMain/
├── androidApp/          # Android application module
├── desktopApp/          # Compose Desktop application
├── rust-core/           # Rust library (FFI to Kotlin via UniFFI / JNI)
├── gradle/
└── build files
```

## Target Platforms

| Platform     | Status / Notes                                      |
|--------------|-----------------------------------------------------|
| Desktop JVM  | Primary (Windows, Linux, macOS)                     |
| Android      | Large screens only (sw ≥ 1000). Phone UI not target |

Android `smallestWidth` ≥ 1000 means tablets / large foldables / desktop-mode phones. Layouts are built for that density range.

## Tech Stack Decisions

- **UI**: Jetpack Compose Multiplatform (Material 3 + custom theme matching the screenshots)
- **Navigation**: Compose Navigation / custom side-rail navigation matching the left sidebar
- **DI**: Koin or manual
- **Networking**: Ktor
- **Serialization**: kotlinx.serialization
- **Auth**: Microsoft OAuth 2.0 device/code flow + Xbox Live / Minecraft services
- **Core engine**: Rust crate for:
  - Parallel downloads of versions/assets
  - Safe process spawning / argument building
  - File system watching / integrity
- **Launching**: Java process management (JVM detection, classpath, natives)

## UI Screens (matching provided designs)

1. **Home** – Hero banner ("The End awaits..."), Play Now, Featured News, What's New, Quick Links, Join Realms
2. **Account Type** – Offline Account vs Microsoft Account selection
3. **New Instance** – Normal vs Modpack choice (with fox/wolf illustrations)
4. **Create Instance** – Loader selection (Vanilla/Fabric/Forge/...), version pickers, RAM slider, Java runtime
5. **File Manager** – Full file browser with .minecraft, mods, resourcepacks, etc. + storage usage
6. **Settings** – Categorized cards (General, Minecraft, Performance, Downloads, Appearance, Experimental, Controls)
7. **Sidebar** – Home / Installations / Skins / Marketplace / Settings / About / File Manager / Account

Colors extracted from designs:
- Background: soft warm cream `#FFF8F0` / `#FDF6ED`
- Accent yellow: `#F5C518` / gold buttons
- Nether reds/oranges for banners
- Card surfaces with soft shadows and rounded corners (20–28 dp)

## Getting Started (Development)

### Prerequisites
- JDK 17+
- Android Studio / IntelliJ IDEA with Compose Multiplatform plugin
- Rust toolchain (for `rust-core`)
- Android SDK (API 34+)

### Build

```bash
# Clone
git clone https://github.com/kiua01/AY-Launcher.git
cd AY-Launcher

# Desktop
./gradlew :desktopApp:run

# Android (device/emulator with large sw)
./gradlew :androidApp:installDebug
```

### Rust core
```bash
cd rust-core
cargo build --release
# Then generate bindings (UniFFI planned)
```

## Roadmap (high level)

- [x] Repo + architecture + UI skeleton matching designs
- [x] Compose theme + main screens (Home, Account, Installations, File Manager, Settings, About)
- [x] Clickable sidebar navigation
- [x] New Instance → Create form flow with live in-memory list
- [x] Domain models + simple repositories (Account / Instance)
- [x] Offline accounts (create, switch, show in UI)
- [x] Microsoft device-code UI flow (simulation only, no secrets)
- [ ] Real Microsoft OAuth + token exchange
- [x] Live Mojang version manifest (Ktor + cache + fallback)
- [x] Create Instance version picker with Releases/Snapshots + loading states
- [x] Persist accounts + instances (Desktop: ~/.aylauncher/state.json)
- [ ] Android persistent storage (Context filesDir)
- [ ] Real instance folders + game directory management
- [ ] Rust core integration for downloads & launch
- [ ] File Manager full implementation (real FS)
- [ ] Mod / modpack installer (Modrinth first)
- [ ] Android packaging & large-screen polish
- [ ] Desktop packaging (jpackage / native)

## Disclaimer

This is an independent third-party launcher.  
You must own a legitimate Minecraft: Java Edition license to play online / with Microsoft accounts.  
Offline mode is provided for convenience (as in many community launchers).

Not affiliated with Mojang, Microsoft, or Craft Studio.

## License

MIT (or as decided later). See LICENSE.

---

**Created for the AY Launcher project.**  
UI references are stored in `docs/assets/` for design fidelity.
