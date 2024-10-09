# 🏥 Blood Hub App

## 🚀 Overview

The **Hospital-Blood Bank Connection App**, built using **Android Studio**, **Kotlin**, and **Firebase**, revolutionizes healthcare coordination by enabling hospitals to request specific blood types, and blood banks to respond instantly based on inventory. This ensures timely blood supply access, improving patient care and optimizing efficiency.

## ✨ Features

- 🩸 **Real-time Blood Requests**: Hospitals request specific blood types and quantities.
- 🔐 **Firebase Integration**: Secure authentication and real-time data exchange.
- 🖥️ **User-Friendly Interface**: Simple, intuitive navigation for staff.
- 💬 **Cloud Messaging**: Real-time communication to reduce delays.

## 📋 Project Description

This application uses **Firebase** as the backend and provides two distinct login options:

1. **Hospital Login**
2. **Blood Bank Login**

### Functionality:

- **Hospital Login**: After successful login, hospitals can raise blood requests, which are sent to nearby blood bank hubs.
- **Blood Bank Login**: Blood banks can view these requests in their request column. If a blood bank accepts the request, it will be removed from the request lists of all other blood banks.
- If a blood bank dismisses the request, the request is deleted from that blood bank's list but remains visible to others.
- **Request Status**: Hospitals can track whether their blood requests have been accepted or not.


## 🛠️ Tech Stack

- **Android Studio**: App development.
- **Kotlin**: Programming language.
- **XML** : Font End Development
- **Firebase**:
    - 🔑 **Authentication and Authorization**: Login and user management.
    - 📂 **Cloud Firestore**: Real-time database.
    - 📩 **Cloud Messaging**: Notifications and updates.
- **Material Design**: Clean, user-friendly interface.

## 🖥️ Work Flow Diagram

Below is a visual representation of the application's workflow, illustrating how hospitals and blood banks interact through the app:

![Work Flow Diagram](https://github.com/user-attachments/assets/b8581575-eea1-4eb5-b708-f30796356396)

## ▶️ Demo Video

Watch a demo of the **Blood Hub App** in action:

[![Demo Video](https://img.youtube.com/vi/your-demo-video-id/maxresdefault.jpg)](https://github.com/user-attachments/assets/5b77cc03-6706-49d3-87a1-264640b08d55)

## 🛠️ Installation

To run this project locally:

1. **Clone the repository**:
    ```bash
   git clone https://github.com/VSurendhar/BloodHub.git
3. Open the project in Android Studio and configure Firebase with your project credentials.
4. Run the app on an emulator or an Android device.
