# PlugPro - Home Services Marketplace (Android Java & XML)

**PlugPro** is an on-demand home service marketplace application inspired by modern Material 3 design paradigms. Built entirely using **Java** and **XML layouts**, adhering to clean MVVM architecture, and integrated with **Google Firebase** (Authentication, Cloud Firestore, Firebase Storage, and Cloud Messaging).

The project is lightweight and configured specifically to be opened, edited, and built directly on an Android phone using **AndroidIDE**, **AIDE**, or on desktop using Android Studio.

---

## 1. Complete Project Structure

```
PlugPro/
├── app/
│   ├── build.gradle
│   ├── google-services.json
│   ├── proguard-rules.pro
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/
│           │   └── com/
│           │       └── plugpro/
│           │           ├── PlugProApplication.java
│           │           ├── data/
│           │           │   ├── model/
│           │           │   │   ├── User.java
│           │           │   │   ├── ServiceProvider.java
│           │           │   │   ├── ServiceCategory.java
│           │           │   │   ├── Booking.java
│           │           │   │   ├── ChatMessage.java
│           │           │   │   ├── ChatChannel.java
│           │           │   │   ├── Review.java
│           │           │   │   └── Favorite.java
│           │           │   └── repository/
│           │           │       ├── AuthRepository.java
│           │           │       ├── ProviderRepository.java
│           │           │       ├── BookingRepository.java
│           │           │       └── ChatRepository.java
│           │           ├── notification/
│           │           │   └── PlugProMessagingService.java
│           │           ├── ui/
│           │           │   ├── adapters/
│           │           │   │   ├── CategoryHorizontalAdapter.java
│           │           │   │   ├── CategoryGridAdapter.java
│           │           │   │   ├── ProviderAdapter.java
│           │           │   │   ├── BookingAdapter.java
│           │           │   │   ├── ChatListAdapter.java
│           │           │   │   ├── MessageAdapter.java
│           │           │   │   ├── ReviewAdapter.java
│           │           │   │   └── OnboardingAdapter.java
│           │           │   ├── auth/
│           │           │   │   ├── SplashActivity.java
│           │           │   │   ├── OnboardingActivity.java
│           │           │   │   ├── LoginActivity.java
│           │           │   │   ├── SignupActivity.java
│           │           │   │   └── ForgotPasswordActivity.java
│           │           │   ├── booking/
│           │           │   │   ├── BookingScheduleActivity.java
│           │           │   │   └── BookingDetailActivity.java
│           │           │   ├── chat/
│           │           │   │   └── ChatActivity.java
│           │           │   ├── customer/
│           │           │   │   ├── MainActivity.java
│           │           │   │   ├── HomeFragment.java
│           │           │   │   ├── ServicesFragment.java
│           │           │   │   ├── BookingsFragment.java
│           │           │   │   ├── ChatListFragment.java
│           │           │   │   └── CustomerProfileFragment.java
│           │           │   ├── favorites/
│           │           │   │   └── FavoritesActivity.java
│           │           │   ├── notifications/
│           │           │   │   └── NotificationsActivity.java
│           │           │   ├── provider/
│           │           │   │   ├── ProviderMainActivity.java
│           │           │   │   ├── ProviderDashboardFragment.java
│           │           │   │   ├── ProviderBookingsFragment.java
│           │           │   │   ├── ProviderAvailabilityActivity.java
│           │           │   │   └── ProviderRegistrationActivity.java
│           │           │   ├── providers/
│           │           │   │   └── ProviderDetailActivity.java
│           │           │   └── search/
│           │           │       └── SearchActivity.java
│           │           └── utils/
│           │               ├── DateTimeUtil.java
│           │               ├── FirebaseUtil.java
│           │               ├── PreferenceHelper.java
│           │               ├── SeedDataUtil.java
│           │               └── ValidationUtil.java
│           └── res/
│               ├── drawable/
│               │   ├── bg_banner_card.xml
│               │   ├── bg_chip_selected.xml
│               │   ├── bg_chip_unselected.xml
│               │   ├── bg_message_received.xml
│               │   ├── bg_message_sent.xml
│               │   ├── bg_search_bar.xml
│               │   ├── ic_arrow_back.xml
│               │   ├── ic_booking.xml
│               │   ├── ic_calendar.xml
│               │   ├── ic_call.xml
│               │   ├── ic_chat.xml
│               │   ├── ic_check_circle.xml
│               │   ├── ic_clock.xml
│               │   ├── ic_heart.xml
│               │   ├── ic_heart_filled.xml
│               │   ├── ic_home.xml
│               │   ├── ic_location.xml
│               │   ├── ic_logo.xml
│               │   ├── ic_notification.xml
│               │   ├── ic_profile.xml
│               │   ├── ic_search.xml
│               │   ├── ic_send.xml
│               │   ├── ic_services.xml
│               │   ├── ic_star.xml
│               │   └── ic_verified.xml
│               ├── layout/
│               │   ├── activity_booking_detail.xml
│               │   ├── activity_booking_schedule.xml
│               │   ├── activity_category_detail.xml
│               │   ├── activity_chat.xml
│               │   ├── activity_favorites.xml
│               │   ├── activity_forgot_password.xml
│               │   ├── activity_login.xml
│               │   ├── activity_main.xml
│               │   ├── activity_notifications.xml
│               │   ├── activity_onboarding.xml
│               │   ├── activity_provider_availability.xml
│               │   ├── activity_provider_detail.xml
│               │   ├── activity_provider_main.xml
│               │   ├── activity_provider_registration.xml
│               │   ├── activity_search.xml
│               │   ├── activity_signup.xml
│               │   ├── activity_splash.xml
│               │   ├── dialog_add_review.xml
│               │   ├── fragment_bookings.xml
│               │   ├── fragment_chat_list.xml
│               │   ├── fragment_customer_profile.xml
│               │   ├── fragment_home.xml
│               │   ├── fragment_provider_bookings.xml
│               │   ├── fragment_provider_dashboard.xml
│               │   ├── fragment_services.xml
│               │   ├── item_booking_card.xml
│               │   ├── item_category_grid.xml
│               │   ├── item_category_horizontal.xml
│               │   ├── item_chat_preview.xml
│               │   ├── item_message_received.xml
│               │   ├── item_message_sent.xml
│               │   ├── item_onboarding_page.xml
│               │   ├── item_provider_card.xml
│               │   └── item_review.xml
│               ├── menu/
│               │   ├── bottom_nav_menu.xml
│               │   └── provider_bottom_nav_menu.xml
│               ├── values/
│               │   ├── colors.xml
│               │   ├── dimens.xml
│               │   ├── strings.xml
│               │   └── themes.xml
│               └── values-night/
│                   └── themes.xml
├── build.gradle
├── settings.gradle
├── gradle.properties
├── firebase-blueprint.json
├── firebase/
│   ├── firestore.rules
│   ├── firestore.indexes.json
│   └── storage.rules
└── admin/
    ├── index.php
    ├── config.php
    └── schema.sql
```

---

## 2. Technologies Used

- **Operating System Platform:** Android (Min SDK 24 / Target SDK 34)
- **Programming Language:** Java 17 (Pure Java, no Kotlin)
- **UI Framework:** Android XML with Google Material Components 3 (`com.google.android.material:material:1.11.0`)
- **Layout Engines:** ConstraintLayout, NestedScrollView, RelativeLayout, LinearLayout
- **Navigation:** BottomNavigationView, FragmentTransactions, ViewPager2
- **Data Architecture:** MVVM (Model - View - Repository pattern)
- **Backend & Database:** Firebase BOM 32.8.0
  - Firebase Authentication (Email/Password)
  - Cloud Firestore (Offline persistence enabled, realtime document listeners)
  - Firebase Storage (Profile pictures, service attachments)
  - Firebase Cloud Messaging (Push notification alerts)
- **Image Pipeline:** Glide 4.16.0 with CircleImageView for avatars
- **Web Admin Panel:** PHP 8, MySQL, Vanilla HTML5/CSS3/JavaScript (Zero React/Vue)

---

## 3. Step-by-Step Guide: Open & Build on Phone (AndroidIDE)

### Step 1: Install AndroidIDE
1. Download **AndroidIDE** from the official F-Droid repository or GitHub releases.
2. Grant storage permissions when prompted.
3. Open AndroidIDE and let the initial bootstrap toolchain install OpenJDK 17 and Android SDK 34.

### Step 2: Place the Code on Device
Copy this `PlugPro` project folder to your Android internal storage path:
`/sdcard/AndroidIDEProjects/PlugPro` or clone it via the built-in terminal:
```bash
cd /storage/emulated/0/AndroidIDEProjects
git clone <your-repo-url> PlugPro
```

### Step 3: Open Project in AndroidIDE
1. Open AndroidIDE, tap **Open Project**, and navigate to `PlugPro`.
2. AndroidIDE will automatically read `settings.gradle` and initialize Gradle project syncing.

### Step 4: Build Debug APK
1. In the top toolbar, tap the **Run (Green Play)** button, or open the terminal inside AndroidIDE and run:
   ```bash
   ./gradlew assembleDebug
   ```
2. The generated debug APK will be located at:
   `app/build/outputs/apk/debug/app-debug.apk`
3. Tap **Install** directly from AndroidIDE to test on your phone.

---

## 4. Firebase Setup & Configuration

1. Visit [Firebase Console](https://console.firebase.google.com/) and click **Add project**. Name it `PlugPro`.
2. Add an **Android app**:
   - Package name: `com.plugpro`
   - App nickname: `PlugPro`
3. Download `google-services.json` and replace `app/google-services.json`.
4. In the Firebase Console:
   - **Authentication**: Enable the **Email/Password** sign-in provider.
   - **Firestore Database**: Click **Create database** (Start in production or test mode).
   - **Firebase Storage**: Click **Get started** to provision the default storage bucket.
5. Deploy security rules from the `firebase/` directory:
   ```bash
   firebase deploy --only firestore:rules,storage
   ```

---

## 5. Security Rules

The Firestore security rules in `firebase/firestore.rules` protect customer and provider data:
- **Users**: Users can read and update only their own profile; admins have full access.
- **Providers**: Publicly readable by all customers; only the provider or an admin can update profile details or availability.
- **Bookings**: Only the assigned customer, assigned provider, or admin can view or modify the booking document.
- **Reviews**: Publicly readable; customers can only submit a review after placing a booking.
- **Chats & Messages**: Restricted strictly to the customer and provider participating in the channel.

---

## 6. How to Create the First Admin

1. Register an account through the app signup screen with email `admin@plugpro.com`.
2. Open Firebase Console -> **Firestore Database** -> `users` collection.
3. Find your user document (matching your Auth UID).
4. Change the `role` field value from `"customer"` to `"admin"`.
5. The security rules and admin features recognize `admin@plugpro.com` and `role == 'admin'` automatically.

---

## 7. How to Create the First Provider

1. Open the app and tap **Sign Up**.
2. Select the **Service Provider** radio button.
3. Enter Name, Email, Phone, and Password.
4. The app immediately takes you to `ProviderRegistrationActivity`:
   - Enter Profession (e.g., *Master Electrician*).
   - Enter Years of Experience (e.g., *8*).
   - Enter Hourly Rate (e.g., *450*).
   - Enter Service Area (e.g., *Metro & Suburbs*).
   - Enter Bio/About.
5. Tap **Complete Registration**. The provider document is created in Firestore with status `pending`.
6. To verify the provider:
   - In Firestore Console or via the Web Admin Panel, toggle `verifiedStatus` to `"verified"`.
   - The provider now receives the green verified badge and appears at the top of search!

---

## 8. How to Test Customer Booking

1. Sign up or log in as a customer.
2. On the **Home** screen, tap on any recommended professional card (e.g., *James Carter*).
3. Review their bio, rating (4.9 ★), jobs done, and available time slots.
4. Tap **Schedule Service**.
5. Select Date and preferred Time Slot, input address, and describe the problem.
6. The screen automatically calculates the total fee (e.g. ₹450 service fee + ₹49 convenience = ₹499).
7. Tap **Confirm Booking**.
8. View the live status timeline in `BookingDetailActivity`.
9. Log in as the provider on another device (or switch mode) to accept the booking and mark it *On The Way*, *Started*, and *Completed*.
10. Once completed, the customer can submit a 1–5 star rating and written review!

---

## 9. Known Limitations

- Real SMS verification requires an SMS gateway (Twilio / Firebase Phone Auth) to be configured with API keys.
- Online payments are scaffolded under `paymentMethod = "Cash on Service"`; Razorpay or Stripe SDKs can be plugged in by swapping the confirmation intent.
