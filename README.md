# 🥫 Shelfy

Shelfy is an Android app that helps you keep track of the food in your kitchen. Scan a barcode or add a product manually, set its expiry date, and Shelfy watches your inventory and notifies you before things go bad. It also doubles as a lightweight shopping list.

## ✨ What it does

- **Barcode scanning** — point the camera at a product barcode, and Shelfy looks it up automatically via the [Open Food Facts](https://world.openfoodfacts.org/) public database (name, brand, image, Nutri-Score).
- **Manual product entry** — no barcode? Add a product by hand with a name, brand, category, quantity, and expiry date.
- **Inventory tracking** — a searchable, filterable grid of everything currently in your kitchen, grouped by category.
- **Expiry notifications** — a background worker checks your inventory daily and sends a notification when a product is about to expire, based on a configurable "days before" warning and a daily alert time. Adding or editing a product also triggers an immediate check, so you're not left waiting for the next scheduled run. Scheduling survives app restarts and device reboots.
- **Shopping list** — a separate categorized list for things you need to buy, independent from your tracked inventory.
- **Consumption stats** — mark a product as consumed or thrown away when you're done with it, and Shelfy keeps a running tally so you can see how much food you're actually using versus wasting.
- **Data export** — export your full inventory to CSV at any time.
- **Settings** — toggle notifications, pick the daily alert time and advance-warning window, and clear all stored data.

## 🛠️ Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose with Material 3
- **Architecture:** MVVM — `ViewModel` + `StateFlow` per screen, repositories mediating between UI and data sources
- **Local persistence:** Room (product inventory, shopping list) and Jetpack DataStore (user preferences)
- **Background work:** WorkManager for scheduled and one-off expiry checks, plus a boot receiver to reschedule after device restarts
- **Networking:** Retrofit + Gson, talking to the Open Food Facts REST API
- **Barcode scanning:** CameraX for the camera pipeline, ML Kit Barcode Scanning for detection
- **Image loading:** Coil
- **Navigation:** Jetpack Navigation for Compose
- **Permissions:** Accompanist Permissions (camera, notifications)

## 📁 Project structure

```
app/src/main/java/com/example/shelfy/
├── data/
│   ├── local/         Room database, DAOs, entities
│   ├── preferences/    DataStore-backed settings
│   ├── remote/         Retrofit API + DTOs (Open Food Facts)
│   └── repository/     Repositories mediating local/remote data
├── di/                 Manual dependency wiring (DatabaseModule, NetworkModule)
├── model/               UI-facing data models (FoodItem, ShoppingItem, categories, ...)
├── notifications/       WorkManager workers, scheduler, boot receiver
└── ui/
    ├── components/      Screens and reusable Composables
    ├── theme/           Colors, typography, theming
    └── viewmodel/       ViewModels per feature area
```

## ✅ Requirements

- Android Studio (a recent version with support for the AGP/Kotlin versions in `gradle/libs.versions.toml`)
- JDK 11+
- An Android device or emulator running **API 24 (Android 7.0) or newer**

## 🚀 Building

```bash
git clone <this-repo-url>
cd mobilki
./gradlew assembleDebug
```

Or open the project in Android Studio and hit Run. No API keys or extra configuration are required — Open Food Facts is a free, public API.

## 📝 Notes

- The project currently ships with the default Android Studio test templates; there isn't real unit/instrumented test coverage yet.
- `minSdk` is 24, `targetSdk` is 36.
