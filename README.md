<div align="center">
    <img width="256" src="ZalithLauncher/src/main/res/drawable/app_name_title.png"></img>
</div>

<div align="center">

[![Android CI](https://github.com/MovTery-IO/MovteryLauncher/actions/workflows/android.yml/badge.svg)](https://github.com/MovTery-IO/MovteryLauncher/actions/workflows/android.yml)
[![License: GPL-3.0](https://img.shields.io/badge/License-GPL--3.0-green.svg)](https://www.gnu.org/licenses/gpl-3.0.html)
[![Discord](https://img.shields.io/discord/1409012263423185039?label=Discord&logo=discord&color=7289DA)](https://discord.gg/yDDkTHp4cJ)

</div>

---

## About This Fork

> [!NOTE]
> This is a **modified fork** of [ZalithLauncher](https://github.com/ZalithLauncher/ZalithLauncher), a Minecraft launcher for Android based on [PojavLauncher](https://github.com/PojavLauncherTeam/PojavLauncher).

This project contains custom UI enhancements, modern Material Design 3 theming, and smooth animations while maintaining full compatibility with the original ZalithLauncher functionality.

---

## Credits

- **Original Developer**: [ZalithLauncher Team](https://github.com/ZalithLauncher)
- **Base Project**: [ZalithLauncher](https://github.com/ZalithLauncher/ZalithLauncher) - Licensed under GPL-3.0
- **PojavLauncher**: [PojavLauncherTeam](https://github.com/PojavLauncherTeam/PojavLauncher)

---

## Modifications & Features

This fork includes the following enhancements:

### UI/UX Improvements
- **Material Design 3** theming with proper color system
- Smooth animated theme switching (light/dark mode transition)
- Spring-based physics animations for natural motion
- Staggered list animations for better visual feedback
- Modernized splash screen with entrance animations
- Refined color palette with better contrast

### Technical Changes
- Added `values-night/` folder for proper dark theme support
- Created custom animation utilities (`AnimationUtils.kt`)
- Implemented `ThemeManager` for animated theme transitions
- Updated layouts with Material Design components

### Excluded Content
- Removed promotional/sponsor-related UI elements
- Simplified about page layout

---

## Screenshots

![Screenshot1](/.github/images/Screenshot_Launcher_Light_EN_US.jpg)
![Screenshot2](/.github/images/Screenshot_Launcher_Dark_EN_US.jpg)
![Screenshot3](/.github/images/Screenshot_Game_EN_US.jpg)

---

## Building

This project requires **Java 11+** and **Android SDK 34**.

```bash
# Clone the repository
git clone https://github.com/MovTery-IO/MovteryLauncher.git

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

---

## License

- **This Fork**: GPL-3.0 (same as original)
- **ZalithLauncher**: [GPL-3.0](https://github.com/ZalithLauncher/ZalithLauncher/blob/main/LICENSE)
- **PojavLauncher**: Based on Boardwalk (Apache 2.0 / GPLv2)

---

## Disclaimer

> [!WARNING]
> This is a **community modification** of the original Zalith Launcher. This is not the official Zalith Launcher. The official website is [zalithlauncher.cn](https://zalithlauncher.cn) (note: .cn domain, not .com).

Minecraft is a trademark of Microsoft Corporation. This project is not affiliated with Microsoft.

---

## Language

- <a href="/README_ZH_CN.md">简体中文</a>丨<a href="/README-ZH_TW.md">繁體中文</a>