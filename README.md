# 📱 Social Media App (Android)

Android client application for the [GoSocialMediaApi](https://github.com/alexistamher/GoSocialMediaApi). Built with **Kotlin** and modern **Jetpack Compose**, following **Clean Architecture** principles, **MVVM**, and **Dependency Injection** to ensure a scalable, maintainable, and thoroughly tested codebase.

---

## 🛠️ Technologies and Tools

- **Language:** Kotlin
- **UI Toolkit:** Jetpack Compose & Material 3
- **Architecture:** Clean Architecture + MVVM
- **Dependency Injection:** Koin (`koin-android`, `koin-androidx-compose`, `koin-test`)
- **Networking:** Ktor Client with Content Negotiation & Kotlinx Serialization
- **Concurrency:** Kotlin Coroutines & Flow
- **Testing:**
  - **Unit Testing:** JUnit 4, MockK, Koin Test, Coroutines Test
  - **UI & End-to-End Testing:** Espresso, AndroidX Compose UI Test, UiAutomator

---

## 🐳 Backend Server (Docker)

This app connects to the [GoSocialMediaApi](https://github.com/alexistamher/GoSocialMediaApi) backend. You can spin up both the database and the Go server easily using Docker:

```bash
# Clone the API repository
git clone https://github.com/alexistamher/GoSocialMediaApi.git
cd GoSocialMediaApi

# Run database and server with Docker Compose
docker compose up --build
```

---

## ⚙️ Configuration & Setup

Before running the application, configure the API server URL in your `local.properties` file:

1. Open or create the `local.properties` file in the root project directory:
   ```properties
   ## local.properties
   BASE_API_URL=http://10.0.2.2:8080
   ```
   > **Note:**
   > - Use `http://10.0.2.2:8080` when testing with the **Android Emulator** (maps to `localhost` on the host machine).
   > - Use your machine's local IP (e.g., `http://192.168.1.X:8080`) or a tunneling URL (e.g., ngrok) when testing on a **physical device**.

2. Sync the project with Gradle files and run the application on an emulator or physical device.

---

## 🧪 Testing

The project includes comprehensive test suites covering unit, integration, and UI automation:

- **JUnit & MockK:** Unit tests for UseCases and ViewModels, isolating business logic with mocked dependencies.
- **Koin Test:** Injects test-specific configurations and mocks into the dependency graph.
- **Espresso & UiAutomator:** Automated functional and UI tests verifying user flows, navigation, and interface interactions.

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumentation & UI Tests (Emulator / Device required)
```bash
./gradlew connectedAndroidTest
```
