# Uplift — Motivational Quotes

Web app with motivational quotes, customizable themes, favorites, and sharing.

**[→ Open site](https://non-s.github.io/Uplift)**

## Features

- 40+ quotes in 5 categories: Motivation, Success, Love, Life, Wisdom
- 5 color themes (Purple, Blue, Green, Orange, Pink)
- Favorites saved in Firebase with anonymous auth -- no sign-up, no data loss
- Native sharing (Web Share API)
- Keyboard navigation (← → or Space)

## Stack

HTML · CSS · JavaScript — no frameworks, no build step.

## Firebase setup

1. Create a Firebase project.
2. Enable Authentication > Anonymous provider.
3. Create a Cloud Firestore database.
4. Copy the Web app config into `firebase-config.js`.
5. Publish `firestore.rules` to protect favorites and quote submissions.

---

[Portfolio](https://non-s.github.io/Portfolio) · [TakStud](https://non-s.github.io/TakStud)
