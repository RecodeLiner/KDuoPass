# KduoPass

KduoPass is a **multi-platform application for managing Time-based One-time Passwords (TOTP)**. It allows you to securely store your service names and secret keys, and it generates the corresponding TOTP codes for two-factor authentication. This application is built using **Kotlin Multiplatform**, leveraging **Jetpack Compose** for the user interface across **Android, iOS, and JVM Desktop**.

## Key Features

*   **Secure Account Storage:** Store your service names and secret keys securely within a local database.
*   **TOTP Code Generation:** Generate time-based one-time passwords from your stored secrets using the **TOTP algorithm**.
*   **Real-time Code Updates:** The application displays the current TOTP code, which updates automatically every 30 seconds, indicated by a progress indicator.
*   **Code and Secret Copying:** Easily copy the generated TOTP code or the secret key to your device's clipboard.
*   **Add New Accounts:** Add new service accounts by providing a service name and its secret key.
*   **Delete Accounts:** Remove stored accounts when they are no longer needed.

## Architecture and Components

KduoPass follows a clean architecture with distinct layers to ensure separation of concerns:

*   **Presentation Layer:**
    *   Built with **Jetpack Compose** for a declarative UI that adapts to different platforms.
    *   Utilises **Material Design 3** components for a modern and consistent user experience.
    *   Manages UI state and logic using **ViewModels** (`AccountViewModel` and `AddAccountViewModel`).
    *   Handles screen navigation using **Arkivanov's Decompose** library (`RootComponent`, `AccountComponent`, `AddAccountComponent`).
    *   Provides composable screens (`AccountListScreen`, `AddAccountScreen`, `RootScreen`) and UI elements (`AccountItem`, `TOTPProgressIndicator`).
*   **Domain Layer:**
    *   Contains the core business logic and use cases:
        *   `AddAccountUseCase`: Adds a new account.
        *   `DeleteAccountUseCase`: Deletes an existing account.
        *   `GenerateCodeUseCase`: Generates the TOTP code from a secret.
        *   `GetAccountsUseCase`: Retrieves the list of stored accounts.
    *   Defines the `Account` data model and the `AccountRepository` interface.
*   **Data Layer:**
    *   Uses **Room Persistence Library** to manage a local SQLite database for storing `AccountEntity` objects.
    *   Defines the `AccountDao` interface for database interactions.
    *   Implements the `AccountRepository` interface (`AccountRepositoryImpl`) to handle data access.
    *   Includes `AccountMapper` to convert between domain models (`Account`) and database entities (`AccountEntity`).
*   **Dependency Injection:**
    *   Utilises **Tatarka Inject** for managing dependencies throughout the application (`AppComponent`, `AndroidPlatformComponent`, `JvmPlatformComponent`).
*   **Utilities:**
    *   `TOTPUtils`: Contains the logic for generating TOTP codes using the provided secret key and the current time.
    *   `Base32Converter`: Provides functions to encode and decode secret keys from Base32 format.

## Platform Specifics

*   **Android:** The Android application entry point is `AppActivity`. It initialises the root component and sets up the Compose UI. The database file is stored as `dpkss.db`.
*   **iOS:** The iOS application is launched via `MainViewController` which hosts the Compose UI.
*   **JVM Desktop:** The JVM Desktop application uses **Jewel UI** components in addition to Compose for the main window. The database file is stored as `user.db` in the system's temporary directory.

## Building the Application

As KduoPass is a Kotlin Multiplatform project, you will need to use **Gradle** to build the application for the target platforms. The specific build steps will depend on the platform you are targeting and your local development environment. Generally, you would use the Gradle tasks provided in the project's `build.gradle.kts` files.

## Key Dependencies

KduoPass relies on the following key libraries:

*   **AndroidX:** Compose UI, Activity Compose, Room Persistence, Appcompat.
*   **Arkivanov Decompose:** For managing component lifecycles and navigation.
*   **Tatarka Inject:** For dependency injection.
*   **Kotlin Coroutines:** For asynchronous programming.
*   **KotlinX Serialization:** For serializing navigation state.
*   **SQLite:** Bundled driver for database access.
*   **Okio:** For efficient I/O operations, used in TOTP code generation.
*   **Jewel UI (JVM Desktop):** A library for building desktop applications with Compose.

This README provides a high-level overview of the KduoPass application. For more detailed information, please refer to the source code.
