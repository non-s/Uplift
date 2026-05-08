# Complete Guide: Publish on Google Play Store

## Pre-Publication Checklist

### 1. Project Settings

#### a) Update AdMob IDs
File: `app/src/main/res/values/strings.xml`

Replace test IDs with your real AdMob IDs:

```xml
<!-- BEFORE (test IDs) -->
<string name="admob_app_id">ca-app-pub-3940256099942544~3347511713</string>
<string name="banner_ad_unit_id">ca-app-pub-3940256099942544/6300978111</string>
<string name="interstitial_ad_unit_id">ca-app-pub-3940256099942544/1033173712</string>

<!-- AFTER (your real IDs) -->
<string name="admob_app_id">ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX</string>
<string name="banner_ad_unit_id">ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX</string>
<string name="interstitial_ad_unit_id">ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX</string>
```

#### b) Check Version
File: `app/build.gradle`

```gradle
defaultConfig {
    applicationId "com.motivacional.frases"
    versionCode 1        // Increment with each new version
    versionName "1.0.0"  // Version visible to users
}
```

#### c) Firebase Configured
- `google-services.json` in the `app/` directory
- Firestore enabled with quotes added
- Firebase Cloud Messaging configured
- Firebase Analytics active

### 2. Create Keystore (Digital Signature)

#### Generate Keystore

```bash
keytool -genkey -v -keystore uplift-release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias uplift
```

**Information requested:**
- Full name: [Your name or company]
- Organizational unit: [Your company or "Independent"]
- Organization: [Organization name]
- City: [Your city]
- State: [Your state]
- Country code: US (or your country code)

#### IMPORTANT: Store Safely

After creating, keep in a safe place:
- The `.jks` file (keystore)
- The keystore password
- The key password (alias)
- The alias name

**IF YOU LOSE THIS INFORMATION, YOU CAN NEVER UPDATE THE APP ON THE PLAY STORE!**

#### Configure in build.gradle

File: `app/build.gradle`

Uncomment and fill in:

```gradle
android {
    // ...

    signingConfigs {
        release {
            storeFile file("../uplift-release.jks")  // Keystore path
            storePassword "YOUR_STORE_PASSWORD"
            keyAlias "uplift"
            keyPassword "YOUR_KEY_PASSWORD"
        }
    }

    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### 3. Generate Release AAB/APK

#### Option A: Android Studio (Recommended)

1. Build > Generate Signed Bundle / APK
2. Choose "Android App Bundle" (AAB)
3. Select your keystore
4. Choose build variant: `release`
5. Click "Finish"

File generated at: `app/build/outputs/bundle/release/app-release.aab`

#### Option B: Command Line

```bash
# Generate AAB (recommended for Play Store)
./gradlew bundleRelease

# OR generate APK
./gradlew assembleRelease
```

### 4. Test the Release Build

```bash
# Install release APK on device
adb install app/build/outputs/apk/release/app-release.apk
```

**Test checklist:**
- [ ] App opens without crashes
- [ ] All screens work
- [ ] Ads appear (use real AdMob test IDs)
- [ ] Firebase connects and loads quotes
- [ ] Favorites work
- [ ] Sharing works
- [ ] Notifications work
- [ ] Dark mode works
- [ ] Copy quote works

## Prepare Assets for Play Store

### 1. App Icon (512x512 px)
- Format: 32-bit PNG
- Size: 512x512 pixels
- No transparency
- Can have rounded corners

### 2. Feature Graphic (1024x500 px)
- Main promotional banner
- Format: PNG or JPG
- Size: 1024x500 pixels
- Appears at the top of the listing

### 3. Screenshots

**Phone (required):**
- Minimum: 2 screenshots
- Maximum: 8 screenshots
- Dimensions: Between 320px and 3840px
- Ratio: 16:9 or 9:16

**Tablet (optional but recommended):**
- Minimum: 2 screenshots
- Same rules as phone screenshots

**Screenshot suggestions:**
1. Main screen with quote list
2. Featured quote card
3. Quote of the day
4. Favorites screen
5. Category filter
6. Dark mode
7. Notification
8. Sharing

### 4. Promotional Video (optional)
- YouTube
- 30 seconds to 2 minutes
- Demonstrates main features

## Create Account on Google Play Console

### Step by Step

1. Go to: https://play.google.com/console
2. Create a developer account
3. Pay the one-time fee of $25 USD
4. Complete your developer profile

## Publish the App

### 1. Create New App

1. Play Console > "Create app"
2. Fill in:
   - Name: "Uplift - Motivational Quotes"
   - Default language: English
   - Type: App
   - Free or paid: Free
   - Required declarations (accept terms)

### 2. Configure Store Listing

#### App Details
- **Title**: Uplift - Motivational Quotes
- **Short description** (80 characters):
  ```
  Daily inspiring quotes with categories, favorites, and sharing
  ```

- **Full description** (up to 4000 characters):
  ```
  Uplift - Your Daily Dose of Motivation

  Receive inspiring quotes every day and transform your routine with positive messages!

  MAIN FEATURES:

  VARIED CATEGORIES
  • Motivation
  • Success
  • Love
  • Life
  • Wisdom

  FAVORITES
  Save your favorite quotes and access them quickly

  SHARE
  Send quotes to friends via WhatsApp, Instagram, Facebook and more

  DAILY NOTIFICATIONS
  Receive a motivational quote every day at your preferred time

  QUOTE OF THE DAY
  Special highlight with the best quote of the day

  DARK MODE
  Modern interface with automatic light and dark theme

  ELEGANT INTERFACE
  Modern design with Material Design 3, smooth animations and intuitive navigation

  EASY COPY
  Copy any quote with one tap to use wherever you want

  FREE WITH NO REGISTRATION
  Download and start using immediately

  ALWAYS UPDATED
  New quotes added regularly

  Download now and transform your days with inspiring messages!
  ```

#### Graphic Resources
- Upload icon (512x512)
- Upload feature graphic (1024x500)
- Upload screenshots

### 3. Categorization

- **Category**: Lifestyle
- **Tags**: quotes, motivation, inspiration, citations

### 4. Contact Information

- Email: [your-contact-email]
- Phone (optional)
- Website (optional)

### 5. Privacy Policy

You NEED a privacy policy. Options:

**Option A: Free Online Generator**
- https://www.privacypolicygenerator.info/
- https://app-privacy-policy-generator.firebaseapp.com/

**Option B: Basic Template**

Host on GitHub Pages or any server and use the URL.

### 6. Content Rating

Answer the questionnaire:
- **Violence**: No
- **Sexual content**: No
- **Obscene language**: No
- **Drug use**: No
- **Other**: No

Expected rating: **Everyone** or **All ages**

### 7. Target Audience

- **Age range**: Everyone (or 13+)
- **Countries**: United States (or worldwide)

### 8. Pricing and Distribution

- **Price**: Free
- **Countries**: Select desired countries
- **Contains ads**: Yes (AdMob)
- **In-app purchases**: No

### 9. App Content

#### Upload the AAB
1. Production > Create new release
2. Upload file: `app-release.aab`
3. Release name: 1.0.0
4. Release notes:
   ```
   Initial launch!

   Modern and elegant interface
   Quote categories
   Favorites system
   Social sharing
   Daily notifications
   Dark mode
   ```

### 10. Review and Publish

1. Complete all required items
2. Click "Review release"
3. Check for no errors
4. Click "Start rollout to production"

## Review Time

- **First version**: 3-7 days
- **Updates**: 1-3 days

## After Publication

### 1. Monitor
- Crashes in Firebase Crashlytics
- Statistics in Play Console
- AdMob revenue

### 2. Respond to Reviews
- Reply to user feedback
- Fix reported bugs

### 3. Update Regularly
- Add new quotes
- Fix bugs
- Add new features

## Monetization with AdMob

### Configure AdMob

1. **Create account**: https://admob.google.com/
2. **Create app** in AdMob
3. **Create ad units**:
   - Banner (320x50)
   - Interstitial (full screen)
4. **Copy IDs** and paste in `strings.xml`

### Optimize Revenue

- **Enable Mediation**: Maximize CPM with multiple networks
- **Test Placement**: Don't overdo ads
- **Analyze Metrics**: eCPM, fill rate, CTR

## Common Issues

### App rejected for policy violation
- Check privacy policy
- Check requested permissions
- Check quote content

### AAB not accepted
- Check digital signature
- Check unique versionCode
- Clean project and recompile

### Ads not appearing
- Wait up to 24h after enabling IDs
- Check IDs were copied correctly
- Test with AdMob test IDs first

## Final Checklist

- [ ] AdMob IDs updated
- [ ] Firebase configured
- [ ] Keystore created and stored
- [ ] AAB generated and tested
- [ ] 512x512 icon created
- [ ] Feature graphic 1024x500 created
- [ ] Screenshots taken (min 2)
- [ ] Descriptions written
- [ ] Privacy policy created
- [ ] Play Console account created
- [ ] $25 fee paid
- [ ] App created in console
- [ ] Rating filled in
- [ ] AAB submitted
- [ ] Release reviewed and submitted

## Congratulations!

Now just wait for Google's approval. Good luck!

---

**Questions?** Check the official documentation: https://developer.android.com/distribute
