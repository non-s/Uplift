# Quick Start

## Essential Steps (15 minutes)

### 1. Install Android Studio
- Download at: https://developer.android.com/studio
- Install and configure the Android SDK

### 2. Configure Firebase (5 min)
1. Go to: https://console.firebase.google.com/
2. Create a new project "Motivational Quotes"
3. Add Android app:
   - Package: `com.motivacional.frases`
4. Download `google-services.json`
5. Place it in the `app/` folder of the project

### 3. Enable Firestore (2 min)
1. In Firebase Console → Firestore Database
2. Create database → Production mode
3. Location: southamerica-east1

### 4. Open Project (3 min)
1. Android Studio → Open → Select the "FrasesMotivacionais" folder
2. Wait for Gradle sync
3. Connect a phone or start an emulator

### 5. Run the App (5 min)
1. Click Run (▶)
2. In the app, tap the 3-dot menu → "Initialize Database"
3. Done! The app is running

## Monetization

### Configure AdMob (10 min)
1. Go to: https://apps.admob.com/
2. Add app "Motivational Quotes"
3. Create 2 ad units:
   - Banner
   - Interstitial
4. Replace IDs in:
   - `AndroidManifest.xml` (APPLICATION_ID)
   - `strings.xml` (banner_ad_unit_id and interstitial_ad_unit_id)

## Publishing

See the full guide in `PUBLICAR.md`

---

**Questions?** Read `README.md` for full documentation
