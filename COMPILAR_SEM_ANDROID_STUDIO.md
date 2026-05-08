# How to Build Uplift WITHOUT Android Studio

No Android Studio on your laptop? No problem! Here are 3 options.

---

## OPTION 1: Hire a Freelancer (Recommended) - $10-30

### Where to Hire:

**Fiverr** (Easiest):
1. Go to: https://www.fiverr.com/
2. Search: "compile android app" or "build android apk"
3. Choose someone with good reviews (4.8+ stars)
4. Typical price: $10-30 USD

**Workana** (Brazil):
1. Go to: https://www.workana.com/
2. Post project: "Compile Simple Android App"
3. Typical price: R$50-150

### What to ask for:

```
I need to compile an Android app that is already done.
- Complete source code (Kotlin + Firebase)
- google-services.json file included
- I need the signed APK for testing
- I need the AAB to publish on Play Store
```

### What to send to the freelancer:

1. **Entire project** (FrasesMotivacionais folder zipped)
2. **google-services.json** is already included in the app/ folder
3. **Instructions**: "Just compile the project, it is complete"

### What you will receive:

- `app-debug.apk` (for testing on phone)
- `app-release.aab` (for publishing on Play Store)
- `.jks` file (keystore for future updates)

---

## OPTION 2: Use GitHub + Online Service (Free, but technical)

### Step 1: Create a GitHub Account

1. Go to: https://github.com/
2. Click "Sign up"
3. Create your free account

### Step 2: Create Repository

1. Click "+" in the top right → "New repository"
2. Name: `uplift-app`
3. Set it to **PRIVATE** (important!)
4. Click "Create repository"

### Step 3: Upload the Project

**Option A - Via Website (Easiest):**

1. In the created repository, click "uploading an existing file"
2. Drag the entire FrasesMotivacionais folder to the browser
3. Wait for the upload
4. Click "Commit changes"

**Option B - Via Command Line:**

```bash
cd C:\Users\YourName\FrasesMotivacionais
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/uplift-app.git
git push -u origin main
```

### Step 4: Use a Build Service

**Codemagic (Recommended):**

1. Go to: https://codemagic.io/
2. Log in with GitHub
3. Click "Add application"
4. Select your `uplift-app` repository
5. Choose "Android App"
6. Configure build (will detect automatically)
7. Click "Start new build"
8. Wait 5-15 minutes
9. Download the generated APK/AAB

**Bitrise:**

1. Go to: https://www.bitrise.io/
2. Log in with GitHub
3. Add app → select repository
4. Follow setup wizard
5. Start build
6. Download artifacts

---

## OPTION 3: Ask Friends/Acquaintances for Help

Do you know anyone who:
- Develops Android apps?
- Has Android Studio installed?
- Studies Computer Science?

### What they need to do:

1. Open Android Studio
2. File → Open → Select the FrasesMotivacionais folder
3. Wait for Gradle to sync
4. Build → Generate Signed Bundle/APK
5. Follow keystore creation wizard
6. Generate APK and AAB

**Time:** 10-20 minutes for someone who already has Android Studio

---

## OPTION 4: Install Android Studio (If your PC can handle it)

### Minimum Requirements:

- Windows 10/11
- 8GB RAM (minimum)
- 8GB disk space
- Decent processor

### If your laptop meets requirements:

1. Download: https://developer.android.com/studio
2. Install (takes 30-60 min)
3. Open the project
4. Wait for sync (first time takes a while)
5. Build → Generate Signed Bundle/APK

**Warning**: Android Studio is heavy and may slow down weak PCs

---

## After Building: How to Install on Your Phone

### Testing with APK:

1. **On your phone:**
   - Settings → Security → Enable "Unknown sources"

2. **Transfer the APK:**
   - Send the APK via WhatsApp, Email, or USB
   - Open the file on your phone
   - Click "Install"
   - Open the app and test!

### Publishing with AAB:

1. Go to Google Play Console
2. Upload the `.aab` file
3. Fill in information
4. Submit for review

---

## My Recommendation

### If you have a budget ($10-30):
→ **Hire on Fiverr**
- Fast (1-2 days)
- No hassle
- You get everything ready

### If you have no budget:
→ **Ask a programmer friend**
- Free
- Faster
- Learn in the process

### If you want to learn:
→ **Try GitHub + Codemagic**
- Free
- Learn about CI/CD
- Useful for future updates

---

## Checklist Before Building

Make sure you have:

- Complete project code
- `google-services.json` file in the `app/` folder
- Firebase configured (Firestore with quotes)
- Decided which option you'll use

---

## Common Issues

### "Freelancer asking for more money"
- Set the price upfront
- Explain that the project is complete, just needs to be compiled
- If they insist, find someone else

### "Online service giving errors"
- Check that google-services.json is in the right place
- Try another service (Codemagic or Bitrise)
- Consider hiring a freelancer

### "Friend couldn't compile it"
- Check if they have an up-to-date Android Studio
- Share this README.md
- Ask them to check for Gradle errors

---

## After Successfully Building

You will have:

1. **app-debug.apk** or **app-release.apk**
   - For testing on your phone
   - Install and check that everything works

2. **app-release.aab**
   - For publishing on Play Store
   - Keep this file safe

3. **file.jks** (keystore)
   - VERY IMPORTANT
   - Keep in a safe place
   - Without it, you can't update the app
   - Make a backup!

---

## Next Steps

1. Build app (you are here)
2. Install and test on phone
3. Configure AdMob (real IDs)
4. Publish on Play Store

Read the `PUBLICAR.md` file when you're ready to launch!

---

**Good luck!** If you have questions, re-read this guide carefully.
