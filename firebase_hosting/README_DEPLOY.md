# 🚀 Deploying Your Referral Web Page on Firebase Hosting (Hindi & English Guide)

This guide shows you how to host your premium app download & referral page on your free Firebase Hosting subdomain (`https://prime-khatab.web.app`).

---

## 🇮🇳 हिंदी निर्देश (Hindi Guide)

### Step 1: APK फ़ाइल को web folder में रखें
1. सबसे पहले अपने Android app का APK बिल्ड करें।
2. उस APK फ़ाइल का नाम बदलकर **`app-release.apk`** रख लें।
3. उस फ़ाइल को इस फोल्डर के `public` फोल्डर के अंदर पेस्ट कर दें (`firebase_hosting/public/app-release.apk`)।

### Step 2: Firebase CLI इंस्टॉल करें
अपने कंप्यूटर पर Terminal या Command Prompt खोलें और ये कमांड चलाएं:
```bash
npm install -g firebase-tools
```

### Step 3: Firebase में लॉगिन करें
लॉगिन करने के लिए ये कमांड चलाएं और अपने Google Account से अनुमति दें:
```bash
firebase login
```

### Step 4: इस फोल्डर में जाएं
Terminal में `firebase_hosting` फोल्डर के अंदर नेविगेट करें:
```bash
cd firebase_hosting
```

### Step 5: Firebase प्रोजेक्ट को लिंक करें
अपने प्रोजेक्ट `prime-khatab` को इस फोल्डर से लिंक करने के लिए यह कमांड चलाएं:
```bash
firebase use prime-khatab
```

### Step 6: Deploy करें!
अपनी वेबसाइट और APK को लाइव करने के लिए यह सिंगल कमांड चलाएं:
```bash
firebase deploy --only hosting
```

**🎉 बधाई हो!** आपकी वेबसाइट अब लाइव है:
`https://prime-khatab.web.app`

अब जब भी आप किसी मित्र को अपना लिंक शेयर करेंगे:
👉 `https://prime-khatab.web.app/register?ref=आपका_कोड`

तो वह सीधा इस डाउनलोड पेज पर आ जाएगा!

---

## 🇬🇧 English Guide

### Step 1: Add APK to the Web Folder
1. Build your production Android APK.
2. Rename the APK file to **`app-release.apk`**.
3. Place this file inside the `public` folder (`firebase_hosting/public/app-release.apk`).

### Step 2: Install Firebase CLI
Run this command globally on your system to install the Firebase tools:
```bash
npm install -g firebase-tools
```

### Step 3: Log In to Firebase
Authenticate with your Firebase account:
```bash
firebase login
```

### Step 4: Navigate to Hosting Folder
Navigate inside the `firebase_hosting` folder:
```bash
cd firebase_hosting
```

### Step 5: Select Your Project
Link this folder to your Firebase Project ID:
```bash
firebase use prime-khatab
```

### Step 6: Deploy to Hosting!
Deploy your beautiful landing page and APK file using:
```bash
firebase deploy --only hosting
```

**🎉 Success!** Your web page is now live:
`https://prime-khatab.web.app`

When you share your link with friends:
👉 `https://prime-khatab.web.app/register?ref=YOUR_CODE`

They will visit this responsive page, copy your code, and download the app directly!
