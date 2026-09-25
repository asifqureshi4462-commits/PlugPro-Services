PlugPro ⚡

A modern home-services marketplace Android application that connects customers with trusted service professionals.

PlugPro helps users discover, compare, schedule, and manage home-service professionals such as electricians, plumbers, AC technicians, cleaners, painters, carpenters, and repair specialists.

> Project Status: Active Development
Platform: Android
Language: Java
UI: XML
Backend: Firebase
Architecture: MVVM-style




---

📱 Overview

PlugPro is designed to make home-service booking simple and convenient.

Customers can:

🔎 Search for services

🛠️ Browse service categories

👨‍🔧 Discover professionals

⭐ Check ratings and reviews

💰 View service pricing

📅 Check availability

📍 Provide a service address

📆 Schedule appointments

💬 Chat with professionals

📞 Contact professionals

❤️ Save favorite professionals

📋 Track bookings

⭐ Submit reviews after completed services


Service professionals can manage their profile, services, availability, booking requests, and completed jobs.


---

✨ Main Features

👤 Customer

User registration and login

Email/password authentication

Google Sign-In support

Forgot password

Profile management

Service search

Service categories

Professional discovery

Professional profiles

Experience and ratings

Availability

Booking system

Booking history

Booking status tracking

Favorites

Customer ↔ professional chat

Call professional

Reviews and ratings

Notifications

Saved addresses

Light/Dark theme


👨‍🔧 Service Provider

Provider registration

Professional profile

Service selection

Pricing management

Experience information

Service area

Availability management

Booking requests

Accept/reject bookings

Booking status updates

Customer details

Customer chat

Completed jobs

Earnings information

Customer reviews

Verification status


🛡️ Admin

The planned admin system will provide:

User management

Provider management

Provider verification

Service/category management

Booking management

Review management

Reports

Notifications

Application settings



---

🔄 Booking Flow

Customer
   │
   ↓
Select Service
   │
   ↓
Browse Professionals
   │
   ↓
View Professional Profile
   │
   ↓
Check Availability
   │
   ↓
Select Date & Time
   │
   ↓
Enter Service Details
   │
   ↓
Confirm Booking
   │
   ↓
Pending
   │
   ↓
Confirmed / Accepted
   │
   ↓
On The Way
   │
   ↓
Started
   │
   ↓
Completed
   │
   ↓
Rating & Review


---

📊 Booking Statuses

PlugPro uses the following booking states:

Pending

Confirmed

Accepted

On The Way

Started

Completed

Cancelled

Rejected



---

🏗️ Technology Stack

Technology	Purpose

Java	Android application development
XML	Android UI layouts
Material Components	Modern UI
RecyclerView	Dynamic lists
Firebase Authentication	User authentication
Cloud Firestore	Application database
Firebase Storage	Image/file storage
Firebase Cloud Messaging	Notifications
Android Navigation	Screen navigation
SharedPreferences	Local preferences
PHP + MySQL	Planned web admin panel



---

📂 Project Structure

PlugPro/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/plugpro/
│           │       ├── data/
│           │       │   ├── model/
│           │       │   ├── repository/
│           │       │   └── datasource/
│           │       │
│           │       ├── ui/
│           │       │   ├── auth/
│           │       │   ├── home/
│           │       │   ├── services/
│           │       │   ├── providers/
│           │       │   ├── booking/
│           │       │   ├── chat/
│           │       │   ├── reviews/
│           │       │   ├── favorites/
│           │       │   ├── profile/
│           │       │   └── provider/
│           │       │
│           │       ├── notification/
│           │       └── utils/
│           │
│           └── res/
│               ├── drawable/
│               ├── layout/
│               ├── menu/
│               ├── mipmap/
│               ├── navigation/
│               ├── values/
│               ├── values-night/
│               └── xml/
│
├── firebase/
│   ├── firestore.rules
│   ├── firestore.indexes.json
│   └── storage.rules
│
├── gradle/
├── build.gradle
├── settings.gradle
├── gradle.properties
├── google-services.json
└── README.md


---

🔥 Firebase Structure

The application is designed around these Firestore collections:

users
providers
services
bookings
reviews
favorites
chats
messages
notifications
settings

Each important document should contain appropriate timestamps such as:

createdAt
updatedAt

Firebase Security Rules are required to restrict users, providers, and administrators to authorized operations.


---

🔐 Security

PlugPro follows these principles:

Firebase Authentication for account security

Firestore Security Rules

Storage Security Rules

Input validation

Role-based access

Provider verification

No hardcoded secret API keys

Authorized access to bookings

Protected user information


Sensitive credentials should never be committed to the repository.


---

💳 Payments

The initial architecture supports:

Cash on service

Payment pending

Payment completed


The payment system is designed so a payment gateway such as Razorpay can be integrated later.

Fake payment-success implementations should not be used in production.


---

📱 Phone Development

PlugPro is intended to be developable from an Android phone.

Recommended workflow:

Phone
 │
 ├── Acode
 │
 ├── AndroidIDE
 │
 ├── Termux
 │
 └── GitHub
        │
        ↓
     PlugPro
        │
        ↓
       APK

The project avoids:

Kotlin

Jetpack Compose

Flutter

React Native

React

Vue

Angular

Unnecessary PC-only tooling



---

🚀 Setup

1. Clone the repository

git clone https://github.com/YOUR_USERNAME/PlugPro.git
cd PlugPro

2. Open the project

Open the project in AndroidIDE or another compatible Android development environment.

3. Configure Firebase

Create a Firebase project and add an Android application.

Download:

google-services.json

Place it inside:

app/google-services.json

Do not commit private credentials or sensitive configuration to a public repository.

4. Enable Firebase Services

Enable the required:

Authentication

Cloud Firestore

Storage

Cloud Messaging


5. Configure Firestore

Deploy or manually configure:

firebase/firestore.rules
firebase/firestore.indexes.json

6. Configure Storage

Apply:

firebase/storage.rules

7. Build

Use AndroidIDE to sync Gradle and build the application.


---

🧪 Testing

Before release, test:

Registration

Login

Logout

Password reset

Profile editing

Service search

Provider profiles

Favorites

Availability

Booking creation

Booking acceptance

Booking cancellation

Booking status updates

Chat

Notifications

Reviews

Image uploads

Firestore permissions

Storage permissions

Network failure

Empty states

Dark mode

Different screen sizes



---

🗺️ Development Roadmap

Phase 1 — Foundation

Project setup

Theme

Navigation

Splash

Onboarding

Authentication


Phase 2 — Customer

Home

Services

Search

Providers

Provider profile

Favorites

Profile


Phase 3 — Booking

Availability

Booking creation

Booking details

Booking history

Booking status


Phase 4 — Communication

Chat

Call

Notifications


Phase 5 — Provider

Provider registration

Dashboard

Booking management

Availability

Services

Earnings

Reviews


Phase 6 — Admin

Admin authentication

Dashboard

User management

Provider verification

Services

Bookings

Reviews

Reports


Phase 7 — Production

Security audit

Performance optimization

Error handling

Testing

Release build

Play Store preparation



---

📌 Current Development Principle

PlugPro should be developed as a real working application, not merely a UI demonstration.

Every major feature should have:

UI
↓
Validation
↓
Business Logic
↓
Repository
↓
Firebase
↓
Real Data

Avoid fake buttons, unnecessary placeholder screens, hardcoded production data, and "Coming Soon" implementations for features that are advertised as available.


---

🤝 Contribution

Contributions are welcome.

Before submitting changes:

1. Test the feature.


2. Follow the existing Java/XML architecture.


3. Keep Firebase access secure.


4. Avoid unnecessary dependencies.


5. Ensure existing features are not broken.




---

📄 License

Choose an appropriate open-source license before making the repository public.

For example:

MIT License

if you want others to use, modify, and distribute the project under the MIT terms.


---

⚡ PlugPro

Find. Book. Connect. Get It Done.

A modern platform connecting customers with trusted home-service professionals.