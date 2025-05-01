import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    id("com.rcl.buildconfig")
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
    androidTarget()

    jvm()

    for (target in listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )) {
        target.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.configure {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.uiToolingPreview)
                implementation(compose.components.resources)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.decompose)
                implementation(libs.decompose.compose)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.coil)
                implementation(libs.coil.network.ktor)
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.kotlin.inject.runtime)
            }
        }

        commonTest.configure {
            dependencies {
                implementation(kotlin("test"))
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.uiTest)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        androidMain.configure {
            dependencies {
                implementation(compose.uiTooling)
                implementation(libs.androidx.activityCompose)
                implementation(libs.kotlinx.coroutines.android)
            }
        }

        jvmMain.configure {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

android {
    namespace = "com.rcl.kduopass"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        targetSdk = 36

        applicationId = "com.rcl.kduopass.androidApp"
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb
            )
            packageName = "KDuoPass"
            packageVersion = "1.0.0"

            linux {
                iconFile.set(project.file("desktopAppIcons/LinuxIcon.png"))
            }
            windows {
                iconFile.set(project.file("desktopAppIcons/WindowsIcon.ico"))
            }
            macOS {
                iconFile.set(project.file("desktopAppIcons/WindowsIcon.ico"))
                bundleID = "com.rcl.kduopass.desktopApp"
            }
        }
    }
}

buildConfig {
    objectName = "InternalBuildConfig"
    packageName = "com.rcl.kduopass"
    buildConfigField("String", "APP_NAME", rootProject.name)
}

dependencies {
    for (dependency in listOf(
        libs.kotlin.inject.compiler,
        libs.room.compiler
    )) {
        with(dependency) {
            add("kspAndroid", this)
            add("kspJvm", this)
            add("kspIosX64", this)
            add("kspIosArm64", this)
            add("kspIosSimulatorArm64", this)
        }
    }
}
