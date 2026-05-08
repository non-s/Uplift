# Complete Guide: Set Up Firebase for Uplift

This guide will walk you through configuring Firebase from scratch, even without programming experience.

## Estimated Time: 15-20 minutes

---

## What you will do:

1. Create a Firebase account (2 min)
2. Create a project (3 min)
3. Add the Android app (5 min)
4. Configure Firestore (5 min)
5. Add quotes to the database (10 min)

---

## PART 1: Create Account and Firebase Project

### Step 1: Access Firebase Console

1. Open your browser
2. Go to: **https://console.firebase.google.com/**
3. Sign in with your Google account
4. If first time, accept the terms

### Step 2: Create New Project

1. Click the **"Add project"** button (or "Create a project")

2. **Screen 1 - Project Name:**
   - Name: `uplift-app` (or any name)
   - Click **"Continue"**

3. **Screen 2 - Google Analytics:**
   - Leave **ENABLED** (recommended)
   - Click **"Continue"**

4. **Screen 3 - Analytics Account:**
   - Select "Default Account for Firebase"
   - Accept the terms
   - Click **"Create project"**

5. **Wait** 30-60 seconds while the project is created

6. Click **"Continue"** when "Your project is ready" appears

---

## PART 2: Add Android App to the Project

### Step 3: Register the Android App

1. On the project home screen, click the **Android** icon
   - Or go to **Project Overview** → **Add app** → **Android**

2. **Fill in the fields:**

   **Android package name (REQUIRED):**
   ```
   com.motivacional.frases
   ```
   **IMPORTANT**: Copy exactly as shown above!

   **App nickname (optional):**
   ```
   Uplift
   ```

   **SHA-1 signing certificate (optional):**
   - Leave blank for now
   - Not required for this project

3. Click **"Register app"**

### Step 4: Download google-services.json

1. On the next screen, click **"Download google-services.json"**

2. **Save the file** in a safe place (e.g., Downloads)

3. **IMPORTANT:** You will need this file later!
   - If using a build service, you will upload this file
   - If hiring someone, you will send this file along

4. Click **"Next"**

5. Click **"Next"** again (ignore the code instructions)

6. Click **"Continue to console"**

---

## PART 3: Configure Firestore Database

### Step 5: Create Firestore Database

1. In the left sidebar, click **"Firestore Database"**

2. Click the **"Create database"** button

3. **Screen 1 - Initialization Mode:**
   - Select: **"Start in production mode"**
   - Click **"Next"**

4. **Screen 2 - Firestore Location:**
   - Select: **"southamerica-east1 (São Paulo)"** (for Brazil)
   - Or choose the one closest to you
   - Click **"Enable"**

5. **Wait** 1-2 minutes while the database is created

### Step 6: Configure Security Rules

1. After the database is created, click the **"Rules"** tab (at the top)

2. **Replace** all content with this:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /quotes/{quote} {
      allow read: if true;
      allow write: if true;
    }
  }
}
```

3. Click **"Publish"**

**Note:** This rule allows read and write for everyone. For production, you should restrict it later.

---

## PART 4: Add Quotes to Firestore

### Step 7: Create Collection

1. Go back to the **"Data"** tab

2. Click **"Start collection"**

3. **Collection ID:**
   ```
   quotes
   ```
   **IMPORTANT**: Type exactly `quotes` (no spaces)

4. Click **"Next"**

### Step 8: Add the First Quote

Now you will add the first quote. Fill in as follows:

**Document ID:**
- Click **"Auto-ID"** (let Firebase generate it)

**Fields** (click "Add field" for each one):

1. **Field 1:**
   - Field name: `text`
   - Type: `string`
   - Value: `Success is the sum of small efforts repeated day after day.`

2. **Field 2:**
   - Field name: `author`
   - Type: `string`
   - Value: `Robert Collier`

3. **Field 3:**
   - Field name: `category`
   - Type: `string`
   - Value: `success`

4. **Field 4:**
   - Field name: `timestamp`
   - Type: `number`
   - Value: `1700000000000`

Click **"Save"**

First quote added!

### Step 9: Add More Quotes

Repeat the process to add more quotes. Here are 9 ready-made quotes:

---

**QUOTE 2:**
- text: `Believe in yourself and everything else will fall into place.`
- author: `Unknown`
- category: `motivation`
- timestamp: `1700000001000`

---

**QUOTE 3:**
- text: `Persistence is the path to success.`
- author: `Charles Chaplin`
- category: `success`
- timestamp: `1700000002000`

---

**QUOTE 4:**
- text: `You are stronger than you think.`
- author: `Unknown`
- category: `motivation`
- timestamp: `1700000003000`

---

**QUOTE 5:**
- text: `Love is the only force capable of turning an enemy into a friend.`
- author: `Martin Luther King`
- category: `love`
- timestamp: `1700000004000`

---

**QUOTE 6:**
- text: `Life is 10% what happens to you and 90% how you react to it.`
- author: `Charles Swindoll`
- category: `life`
- timestamp: `1700000005000`

---

**QUOTE 7:**
- text: `Be the change you wish to see in the world.`
- author: `Mahatma Gandhi`
- category: `wisdom`
- timestamp: `1700000006000`

---

**QUOTE 8:**
- text: `The only way to do great work is to love what you do.`
- author: `Steve Jobs`
- category: `success`
- timestamp: `1700000007000`

---

**QUOTE 9:**
- text: `Don't count the days, make the days count.`
- author: `Muhammad Ali`
- category: `motivation`
- timestamp: `1700000008000`

---

**QUOTE 10:**
- text: `Great things never come from comfort zones.`
- author: `Unknown`
- category: `motivation`
- timestamp: `1700000009000`

---

### Quick Tip

To add each quote:
1. Click **"Add document"** (button at the top)
2. Leave ID as auto
3. Add the 4 fields (text, author, category, timestamp)
4. Save
5. Repeat

---

## PART 5: Verify Configuration

### Step 10: Test that it's working

1. In the left menu, go to **"Firestore Database"**

2. You should see:
   - Collection `quotes`
   - At least 10 documents inside it
   - Each document with 4 fields

3. Click on a document to verify all fields are filled in

---

## PART 6: Enable Cloud Messaging (Notifications)

### Step 11: Enable FCM

1. In the left menu, click the **gear icon** → **Project settings**

2. Go to the **"Cloud Messaging"** tab

3. If it asks to enable, click **"Enable"**

4. Done! Notifications are now configured

---

## Final Checklist

Before continuing, verify:

- Firebase project created
- Android app registered with package `com.motivacional.frases`
- `google-services.json` file downloaded and saved
- Firestore Database created
- Security rules configured
- `quotes` collection created
- At least 10 quotes added
- Cloud Messaging enabled

---

## Next Steps

Now that Firebase is configured, you need to:

1. **Build the app** with the `google-services.json` file
   - Read: `COMPILAR_SEM_ANDROID_STUDIO.md`

2. **Configure AdMob** (to monetize)
   - Read: AdMob section in `PUBLICAR.md`

3. **Test the app** on your phone

4. **Publish on Play Store**

---

## IMPORTANT: The google-services.json File

**What to do with this file:**

1. If you are building yourself:
   - Place it in the `app/` folder of the project

2. If hiring someone to build:
   - Send this file along with the project code

3. If using an online service:
   - Upload this file when prompted

**Never:**
- Share publicly (GitHub, social media)
- Lose this file (make a backup)

---

## Extra Tips

### How to Add More Quotes Later

1. Access Firebase Console
2. Go to Firestore Database
3. Click "Add document"
4. Fill in the fields
5. Save

### Available Categories

Use only these categories (they are configured in the app):
- `motivation`
- `success`
- `love`
- `life`
- `wisdom`

### Where to Find More Quotes

- BrainyQuote.com
- GoodReads.com
- Pinterest (search "motivational quotes")
- Instagram (motivation pages)

**Note on copyrights**: Use public domain quotes or quotes from classic authors

---

## Common Issues

### "Can't find the Android button"
- Make sure you are on the project home page
- Look for "Add app" or the Android icon

### "Error creating collection"
- Make sure you typed `quotes` exactly (lowercase, no spaces)
- Try a different browser

### "Don't know how to fill in the fields"
- `text` field: the quote itself (string)
- `author` field: author's name (string)
- `category` field: one of the 5 categories (string)
- `timestamp` field: large number, e.g. 1700000000000 (number)

### "Lost the google-services.json"
- Go to Project Settings → Your apps
- Click the 3 dots on the Android app → Download google-services.json

---

## Configuration Complete!

Your Firebase is 100% configured.

**Next step:** Open `COMPILAR_SEM_ANDROID_STUDIO.md` to see how to build the app.

---

**Questions?** Re-read this guide carefully — each step is important!
