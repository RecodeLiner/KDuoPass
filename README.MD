# KDuoPass

KDuoPass is a cross-platform, open-source two-factor authentication (2FA) application built using **Kotlin Multiplatform** and **Jetpack Compose**. It allows users to manage and generate Time-Based One-Time Passwords (TOTP) for their accounts securely across **Android**, **iOS**, **Desktop** (Windows, macOS, Linux), and potentially other platforms. The app supports importing/exporting accounts, a clean user interface, and robust data persistence using Room database.

## Features

- **TOTP Generation**: Generate secure 6-digit TOTP codes for 2FA based on user-provided secrets.
- **Cross-Platform Support**: Runs on Android, iOS, and Desktop (Windows, macOS, Linux) with a consistent UI/UX.
- **Account Management**:
    - Add new accounts with service name and secret key.
    - Delete individual accounts or clear all accounts.
    - Import/export accounts in JSON format for easy backup and transfer.
- **Real-Time Code Refresh**: Automatically refreshes TOTP codes every 30 seconds with a visual progress indicator.
- **Clipboard Support**: Copy TOTP codes or secrets to the clipboard with a single tap/click.
- **Responsive UI**: Built with Jetpack Compose for a modern, adaptive, and theme-aware interface.
- **Localization**: Supports multiple languages (e.g., English and Russian).
- **Secure Storage**: Uses Room database with SQLite for persistent and secure account storage.
- **Dependency Injection**: Utilizes Koin for modular and testable dependency management.
- **Custom Gradle Plugin**: Includes a custom `buildConfig` plugin to generate build-time constants.

## Screenshots

![Mobile Screenshot](screenshots/Mobile.png)
![Desktop Screenshot](screenshots/Desktop.png)

## Getting Started

### Prerequisites

- **JDK**: Version 17 or higher.
- **Gradle**: Version 8.0 or higher (included in the wrapper).
- **Android SDK**: For Android builds (API 21+).
- **Xcode**: For iOS builds (required for iOS simulator or device deployment).
- **Kotlin Multiplatform Plugin**: Ensure your IDE (e.g., IntelliJ IDEA or Android Studio) has the Kotlin Multiplatform plugin installed.

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/RecodeLiner/KDuoPass.git
   cd KDuoPass
   ```

2. **Set Up the Project**:
    - Open the project in **IntelliJ IDEA** or **Android Studio**.
    - Sync the project with Gradle to download dependencies.

3. **Build the Project**:
   ```bash
   ./gradlew build
   ```

4. **Run the App**:
    - **Android**: Connect an Android device/emulator and run:
      ```bash
      ./gradlew :composeApp:assembleRelease
      ```
    - **Desktop**: Run the desktop app:
      ```bash
      ./gradlew :composeApp:run
      ```
    - **iOS**: Open the iOS target in Xcode via the generated project in `composeApp/iosApp` and run on a simulator or device.

### Project Structure

- **composeApp**: Contains the main application code, including:
    - **src/commonMain**: Shared Kotlin code for all platforms (UI, business logic, database).
    - **src/androidMain**: Android-specific code (e.g., AndroidManifest, platform-specific DI).
    - **src/jvmMain**: Desktop-specific code (e.g., Swing integration, file system handling).
    - **src/iosMain**: iOS-specific code (e.g., UIKit integration).
- **buildConf**: Custom Gradle plugin for generating build configuration constants.
- **gradle**: Gradle wrapper and configuration files.

### Key Technologies

- **Kotlin Multiplatform**: For sharing code across platforms.
- **Jetpack Compose**: For building the UI.
- **Room Database**: For local data persistence.
- **Decompose**: For navigation and component-based architecture.
- **Kotlinx Serialization**: For JSON-based import/export.
- **Kotlin-inject**: For dependency injection.
- **FileKit**: For cross-platform file handling.
- **Gradle**: For build automation with a custom plugin.

## Usage

1. **Add an Account**:
    - Navigate to the "Add Account" screen via the floating action button.
    - Enter the service name and secret key (Base32-encoded).
    - Save to generate TOTP codes.

2. **View TOTP Codes**:
    - The main screen displays all accounts with their current TOTP codes.
    - Codes refresh every 30 seconds, with a progress indicator showing the remaining time.

3. **Copy Codes/Secrets**:
    - Click the "Copy Code" or "Copy Secret" buttons to copy to the clipboard.
    - A snackbar confirms the action.

4. **Import/Export Accounts**:
    - Go to the "Settings" screen.
    - Export accounts to a JSON file or import accounts from a JSON file (clean or append modes).

5. **Delete Accounts**:
    - Delete individual accounts from the main screen.
    - Optionally, clear all accounts during import (clean mode).

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes and commit (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request.

Please ensure your code follows the project's coding style and includes tests where applicable.

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

## Testing

The project includes unit tests and UI tests:

- **Unit Tests**: Located in `composeApp/src/commonTest`.
- **UI Tests**: Basic Compose UI tests (e.g., `ComposeTest.kt`).
- Run tests with:
  ```bash
  ./gradlew test
  ```
  
## Acknowledgments

- [JetBrains](https://www.jetbrains.com/) for Kotlin and Compose Multiplatform.
- [Arkivanov Decompose](https://github.com/arkivanov/Decompose) for navigation.
- [Vinceglb FileKit](https://github.com/vinceglb/FileKit) for file handling.
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) for JSON support.

## Contact

For questions or feedback, please open an issue on the [GitHub repository](https://github.com/RecodeLiner/KDuoPass) or contact by email.

---

*Built with ❤️ using Kotlin Multiplatform*