# 📱 TrackIt – Expense Tracking Android App  
**Track • Manage • Analyze Your Expenses**

TrackIt is a modern Android application designed to help users track, manage, and analyze their daily expenses efficiently.  
Built using **Kotlin** and **Jetpack Compose**, the app focuses on simplicity, clarity, and performance while following **clean MVVM architecture**.

**Android • Kotlin • Jetpack Compose • MVVM • Firebase Auth • Room (Planned)**

📥 Download APK • ✨ Features • 📸 Screenshots • 🎥 Demo • ⚙️ Installation

---

## 🎯 About

TrackIt helps users maintain a clear overview of their daily spending by allowing them to record, categorize, and review expenses in a simple and intuitive way.

The app includes **secure authentication (Email/Password & Google Sign-In)** and is designed with scalability in mind, making it suitable for future enhancements such as cloud sync and analytics.

This project was built as a **resume-grade / internship assignment project**, focusing on **modern Android best practices** rather than just UI implementation.

---

## ✨ Features

### 🔐 Authentication
- Login & Sign-up using **Email & Password**
- **Google Sign-In** using Firebase Authentication
- Secure and user-friendly onboarding flow

### 💸 Expense Management
- Add, update, and delete expense records
- Category-wise expense tracking:
  - Food
  - Travel
  - Shopping
  - Bills
  - Others
- View total spending at a glance

### 📊 Expense Overview
- Clean list of expenses
- Category-based organization
- Real-time UI updates using state management

### 🎨 User Interface
- Fully built with **Jetpack Compose**
- Uses **Material Design 3**
- Responsive layout for different screen sizes
- Clean spacing, typography, and consistent colors

---

## 📸 Screenshots

### Authentication & Empty State

| Login Screen | Sign Up Screen | Empty State |
|-------------|---------------|-------------|
 
<img src="https://github.com/user-attachments/assets/686a5149-44e1-4029-8afb-a68fb08cd6fa" width="220"/> <img src="https://github.com/user-attachments/assets/9aa5ecb8-336f-4d2a-8eff-b41fdc571cc6" width="220"/> <img src="https://github.com/user-attachments/assets/c173f873-d5a2-416b-b39d-d420bfe289ba" width="220"/> 


<br/>

### Main Flow

| Home Screen | Add Expense |
|------------|-------------|

<img src="https://github.com/user-attachments/assets/60014947-23a8-4b0d-91c7-c1b96f86761c" width="240"/> <img src="https://github.com/user-attachments/assets/55025b0c-6954-4ef3-9c31-996cced15233" width="240"/> 



---

## 🎥 Video Demo

📺 Watch TrackIt in action:

▶️ **[Click here to watch the demo video](https://drive.google.com/file/d/1p3tQ-U9pYO5KnFQruKq0geJYKnZE8znC/view?usp=drive_link)**

**Demo includes:**
- App launch & authentication flow
- Adding an expense
- Expense list updates
- Category selection
- Overall app walkthrough

---

## 🛠️ Tech Stack

| Component | Technology |
|---------|------------|
| Language | Kotlin |
| UI | Jetpack Compose |
| Design System | Material 3 |
| Architecture | MVVM |
| State Management | State Hoisting / UDF |
| Authentication | Firebase Auth (Email & Google) |
| Navigation | Navigation Compose |
| Local Storage | Room Database (Planned) |
| Build System | Gradle (Kotlin DSL) |

---

## 🏗️ Architecture & Project Structure

TrackIt follows **Clean MVVM architecture** with a clear separation between UI, data, and navigation layers.
The structure is designed to be **scalable, testable, and easy to maintain**.

```text
TrackIt/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/
│   │   │   │   └── com/
│   │   │   │       └── practice/
│   │   │   │           └── trackit/
│   │   │   │
│   │   │   │               ├── component/
│   │   │   │               │   └── GoogleButton.kt
│   │   │   │
│   │   │   │               ├── data/
│   │   │   │               │   ├── model/
│   │   │   │               │   │   └── Expense.kt
│   │   │   │               │   └── repository/
│   │   │   │               │       └── ExpenseRepository.kt
│   │   │   │
│   │   │   │               ├── ui/
│   │   │   │               │   ├── auth/
│   │   │   │               │   │   ├── AuthState.kt
│   │   │   │               │   │   ├── AuthViewModel.kt
│   │   │   │               │   │   ├── LoginScreen.kt
│   │   │   │               │   │   ├── SignupScreen.kt
│   │   │   │               │   │   └── SplashScreen.kt
│   │   │   │               │   │
│   │   │   │               │   ├── dashboard/
│   │   │   │               │   │   ├── DashboardScreen.kt
│   │   │   │               │   │   ├── DashboardViewModel.kt
│   │   │   │               │   │   └── EmptyExpenseStateScreen.kt
│   │   │   │               │   │
│   │   │   │               │   ├── expense/
│   │   │   │               │   │   └── AddExpenseScreen.kt
│   │   │   │               │   │
│   │   │   │               │   ├── navigation/
│   │   │   │               │   │   ├── AppNavGraph.kt
│   │   │   │               │   │   └── AppRoutes.kt
│   │   │   │               │   │
│   │   │   │               │   └── theme/
│   │   │   │               │       ├── Color.kt
│   │   │   │               │       ├── Theme.kt
│   │   │   │               │       └── Type.kt
│   │   │   │
│   │   │   │               └── MainActivity.kt
│   │   │   │
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   ├── androidTest/
│   │   └── test/
│   │
│   ├── build.gradle.kts
│   └── proguard-rules.pro
│
├── gradle/
├── gradle.properties
├── gradle-wrapper.properties
├── settings.gradle.kts
├── libs.versions.toml
└── local.properties
```


### Key Benefits
- Clear separation of concerns
- Lifecycle-aware UI
- Easy to test and maintain
- Scalable for future backend integration

---

## 🧪 Error Handling & UX

- Input validation for expense amount and category
- Graceful empty states (no expenses yet)
- User-friendly error messages
- Loading indicators for async operations

---

## 🔐 Permissions Used

- No unnecessary permissions
- App respects user privacy
- Authentication handled securely via Firebase

---

## 🔮 Future Enhancements

- 💾 Offline storage using Room Database
- ☁️ Cloud sync with backend
- 📈 Expense analytics with charts
- 🌙 Dark mode support
- 📤 Export expenses as PDF / CSV

---

## 🚀 How to Run the Project

```bash
git clone https://github.com/CodeWithSangeeta/TrackIt
cd trackit


