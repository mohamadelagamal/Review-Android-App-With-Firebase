<!-- Banner Image -->
![Banner](https://github.com/user-attachments/assets/983cb002-d6ba-44f7-aec5-d57d5bb87c60)

# Review Android App with Firebase

[![Build Status](https://codefresh.io/wp-content/uploads/2023/07/Build_Success_2.jpg)](https://github.com/mohamadelagamal/Review-Android-App-With-Firebase)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Fastlane](https://img.shields.io/badge/fastlane-enabled-blueviolet)](https://fastlane.tools/)
[![Jenkins](https://img.shields.io/badge/jenkins-integrated-blue)](https://www.jenkins.io/)

A robust Android application that enables users to post and read reviews in real-time, powered by Firebase services and enhanced by automated CI/CD pipelines using **Fastlane** and **Jenkins**. Enjoy a seamless experience with multiple build types, product flavors, and modern local storage via Shared Preferences. The app also supports both Light and Dark modes along with full Arabic and English language support.

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

**Review Android App with Firebase** is designed for simplicity and high performance. It combines the power of Firebase—offering Authentication, Realtime Database, and Cloud Firestore—with local storage using Shared Preferences. The project is continuously delivered and integrated via Fastlane and Jenkins, ensuring smooth deployments across development, staging, and production environments. Additionally, the app is designed with modern UI principles in mind, supporting both Light and Dark modes, and is fully localized for Arabic and English users.

---

## 🚀 Features

- **Firebase Authentication:** Secure user sign-in.
- **Realtime Database:** Live updates for review data.
- **Cloud Firestore:** Flexible and scalable cloud storage.
- **Shared Preferences:** Fast, reliable local storage for user settings.
- **CI/CD Integration:** Fully automated builds and deployments using **Fastlane** and **Jenkins**.
- **Multiple Build Types & Flavors:** Customize builds for Debug, Release, and environment-specific configurations.
- **Modern UI/UX:** Intuitive design with support for Light Mode and Dark Mode.
- **Localization:** Fully supports Arabic and English languages.

---

## 🎨 Screenshots & Demo

Experience the app through these visuals:

<table>
  <tr>
    <td align="center">
      <b>Login Screen</b><br>
      <img src="https://github.com/user-attachments/assets/37c32436-4656-40b7-8275-ba31638eea9e" alt="Login Screen" width="200"/>
    </td>
    <td align="center">
      <b>Register Screen</b><br>
      <img src="https://github.com/user-attachments/assets/503e9d94-e44a-4af3-8ba3-687efdbd9b54" alt="Register Screen" width="200"/>
    </td>
    <td align="center">
      <b>Home Screen</b><br>
      <img src="https://github.com/user-attachments/assets/912e332b-bd81-45ed-a1f8-eea574939bd50" alt="Home Screen" width="200"/>
    </td>
    <td align="center">
      <b>AddBottom Screen</b><br>
      <img src="https://github.com/user-attachments/assets/d0e04b7c-6ef4-4b2b-87bf-625aea8b4024" alt="AddBottom Screen" width="200"/>
    </td>
    <td align="center">
      <b>Search Detail</b><br>
      <img src="https://github.com/user-attachments/assets/4d98f51c-4652-489e-bd17-3cfa62978a8c" alt="Search Detail" width="200"/>
    </td>
    <td align="center">
      <b>User Profile</b><br>
      <img src="https://github.com/user-attachments/assets/db493e10-9894-48aa-820e-6a8be09fdbe8" alt="User Profile" width="200"/>
    </td>
    <td align="center">
      <b>Settings (Shared Preferences)</b><br>
      <img src="https://github.com/user-attachments/assets/4afb2fba-9dd5-43c4-9019-eedbb8fa1572" alt="Settings" width="200"/>
    </td>
  </tr>
</table>

> **Demo Video:** [Watch the demo](https://youtube.com/shorts/IQPEizB84nA?si=KarG_n91O5EWv-Oc) <!-- Replace with your demo video link -->

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
