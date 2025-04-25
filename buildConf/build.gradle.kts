plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("buildConfigPlugin") {
            id = "com.rcl.buildconfig"
            implementationClass = "BuildConfigPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    //noinspection UseTomlInstead
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.20")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.1.20")
}