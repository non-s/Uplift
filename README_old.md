# Motivational Quotes - Android App

Android app for motivational quotes with Firebase and AdMob monetization.

## Features

- List of motivational quotes organized by category
- Favorites system
- Social sharing
- Firebase Firestore (cloud database)
- AdMob monetization (banners + interstitial ads)
- Automatic daily notifications
- Modern Material Design interface

## Setup

### 1. Configure Firebase

1. Go to the [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" and follow the steps
3. After creating the project, click "Add app" and select Android
4. Fill in the details:
   - **Package name**: `com.motivacional.frases`
   - **App nickname**: Motivational Quotes
   - **SHA-1**: (optional for this project)
5. Download the `google-services.json` file
6. **IMPORTANT**: Copy `google-services.json` into the `app/` folder of the project

### 2. Enable Firebase Services

In the Firebase Console:

1. **Firestore Database**:
   - In the left menu, go to "Firestore Database"
   - Click "Create database"
   - Choose "Production" mode
   - Select location (southamerica-east1 for Brazil)

2. **Firestore Security Rules**:
   ```
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /quotes/{quote} {
         allow read: if true;
         allow write: if false;
       }
     }
   }
   ```
   (Allows public reads, writes only via console)

3. **Cloud Messaging** (for notifications):
   - Already enabled by default when you create the project

### 3. Configure AdMob

1. Go to the [AdMob Console](https://apps.admob.com/)
2. Click "Apps" > "Add app"
3. Fill in:
   - **App name**: Motivational Quotes
   - **Platform**: Android
4. Note the **App ID** that will be generated
5. Create two ad units:
   - **Banner**: for the bottom of the screen
   - **Interstitial**: to show between user actions

6. **Replace test IDs with real IDs**:

   In `app/src/main/AndroidManifest.xml`:
   ```xml
   <meta-data
       android:name="com.google.android.gms.ads.APPLICATION_ID"
       android:value="YOUR_ADMOB_APP_ID"/>
   ```

   In `app/src/main/res/values/strings.xml`:
   ```xml
   <string name="banner_ad_unit_id">YOUR_BANNER_ID</string>
   <string name="interstitial_ad_unit_id">YOUR_INTERSTITIAL_ID</string>
   ```

### 4. Seed the Database with Quotes

After setting everything up:

1. Open the app
2. In the top menu (three dots), click "Initialize Database"
3. This will add 10 initial quotes to Firestore

### 5. Add More Quotes

You can add quotes directly in the Firebase Console:

1. Go to Firestore Database
2. Click "Add collection"
3. Collection name: `quotes`
4. Add documents with the fields:
   - `text` (string): Quote text
   - `author` (string): Author
   - `category` (string): motivation, success, love, life, or wisdom
   - `timestamp` (number): Current timestamp

## Project Structure

```
app/
├── data/
│   ├── model/          # Data models (Quote, Category)
│   └── repository/     # QuoteRepository (Firebase communication)
├── ui/
│   ├── adapters/       # QuoteAdapter (RecyclerView)
│   ├── viewmodel/      # QuoteViewModel (business logic)
│   └── MainActivity    # Main screen
├── services/           # Firebase Messaging Service
└── utils/              # Helpers (AdMob, Favorites, Notifications)
```

## Monetization Strategy

### Banner Ads
- Permanently displayed at the bottom of the screen
- Lower CPM, but always visible

### Interstitial Ads
- Shown every 5 user actions:
  - Favoriting a quote
  - Sharing a quote
- Higher CPM, but less frequent

### Tips for Maximizing Revenue

1. **Publish on Google Play**
2. **Promote the app**:
   - Social media (Instagram, TikTok, Facebook)
   - WhatsApp/Telegram groups
   - Create motivational content
3. **Update regularly**:
   - Add new quotes weekly
   - Keep content fresh
4. **Engagement**:
   - Encourage sharing
   - Use daily notifications (but don't overdo it)
5. **Optimize for ASO** (App Store Optimization):
   - Descriptive title
   - Keywords: quotes, motivation, inspiration
   - Attractive screenshots

## Build and Run

1. Open the project in Android Studio
2. Wait for Gradle to sync
3. Connect an Android device or use an emulator
4. Click Run (▶)

## Planned Features

- [ ] Home screen widget
- [ ] Dark mode
- [ ] Quote search
- [ ] Custom categories
- [ ] Google login (save favorites to the cloud)
- [ ] Share as image
- [ ] Usage statistics

## Important Notes

- **Test ads**: Use test IDs during development
- **AdMob policies**: Never click on your own ads
- **Content**: Add original quotes and public domain quotes
- **Privacy**: Add a privacy policy before publishing

## License

This project is free for personal and commercial use.

---

**Built with Claude Code**
