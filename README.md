<!-- Banner Image -->
![Banner](![3dUkjUHTGjR3_1024_500](https://github.com/user-attachments/assets/983cb002-d6ba-44f7-aec5-d57d5bb87c60))

# Review Android App with Firebase

[![Build Status](https://codefresh.io/wp-content/uploads/2023/07/Build_Success_2.jpg)
[![License: MIT]()](LICENSE)
[![Fastlane]()](https://fastlane.tools)
[![Jenkins]()](https://www.jenkins.io/)

![App Logo](![ic_launcher_prod](https://github.com/user-attachments/assets/017cd5d5-64d3-488b-86c1-1789605521d4))

A robust Android application that enables users to post and read reviews in real-time, powered by Firebase services and enhanced by automated CI/CD pipelines using **Fastlane** and **Jenkins**. Enjoy a seamless experience with multiple build types, product flavors, and modern local storage via Shared Preferences.

---

## 📚 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Screenshots & Demo](#screenshots--demo)
- [CI/CD Pipeline](#cicd-pipeline)
- [Build Types & Flavors](#build-types--flavors)
- [Getting Started](#getting-started)
- [Firebase Integration](#firebase-integration)
- [Local Data Storage](#local-data-storage)
- [Tech Stack](#tech-stack)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## 🔍 Overview

**Review Android App with Firebase** is designed for simplicity and performance. It combines the power of Firebase—offering Authentication, Realtime Database, and Cloud Firestore—with local storage using Shared Preferences. The project is continuously delivered and integrated via Fastlane and Jenkins, ensuring smooth deployments across development, staging, and production environments.

---

## 🚀 Features

- **Firebase Authentication:** Secure user sign-in.
- **Realtime Database:** Live updates for review data.
- **Cloud Firestore:** Flexible and scalable cloud storage.
- **Shared Preferences:** Fast, reliable local storage for user settings.
- **CI/CD Integration:** Fully automated builds and deployments using **Fastlane** and **Jenkins**.
- **Multiple Build Types & Flavors:** Customize builds for Debug, Release, and environment-specific configurations.
- **Modern UI/UX:** Intuitive and visually appealing design for an enhanced user experience.

---
![Screenshot_٢٠٢٥٠٣٠٦-٠٣١٠١٥](https://github.com/user-attachments/assets/9fa77938-dc92-418a-bc1e-887b975444c2)

## 🎨 Screenshots & Demo

Experience the app through these visuals:

- **Home Screen:**  
  ![Home Screen](![Screenshot_٢٠٢٥٠٣٠٦-٠٣٠٧٣٧](https://github.com/user-attachments/assets/912e332b-bd81-45ed-a1f8-eea574939bd5))

- **Search Detail:**  
  ![Search Detail](![Screenshot_٢٠٢٥٠٣٠٦-٠٣٠٨٤١](https://github.com/user-attachments/assets/4d98f51c-4652-489e-bd17-3cfa62978a8c))

- **User Profile:**  
  ![User Profile](![Screenshot_٢٠٢٥٠٣٠٦-٠٣٠٩٣٦](https://github.com/user-attachments/assets/db493e10-9894-48aa-820e-6a8be09fdbe8))

- **Settings (Shared Preferences):**  ![Settings](![Screenshot_٢٠٢٥٠٣٠٦-٠٣١٠١٥](https://github.com/user-attachments/assets/4afb2fba-9dd5-43c4-9019-eedbb8fa1572))

> **Demo Video:** [Watch the demo](https://www.youtube.com/) <!-- Replace with your demo video link -->

---

## 🤖 CI/CD Pipeline

Our automated CI/CD setup ensures every commit is tested and built:

- **Fastlane:** Automates builds, tests, and deployments to the Play Store.  
- **Jenkins:** Continuously monitors commits, triggers builds, and performs quality checks.

---

## 🛠 Build Types & Flavors

Our project supports versatile build configurations:

### Build Types
- **Debug:** For development and testing.
- **Release:** Optimized for production with code obfuscation and minification.

### Product Flavors
- **Development:** Appended with `.dev` and optimized for local testing.
- **Staging:** For pre-production trials with dedicated settings.
- **Production:** The final release version for end users.

*Example snippet from `build.gradle`:*
```groovy
android {
    buildTypes {
        debug {
            // Debug configuration
        }
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    flavorDimensions "version"
    productFlavors {
        development {
            dimension "version"
            applicationIdSuffix ".dev"
            versionNameSuffix "-dev"
        }
        staging {
            dimension "version"
            applicationIdSuffix ".staging"
            versionNameSuffix "-staging"
        }
        production {
            dimension "version"
        }
    }
}
