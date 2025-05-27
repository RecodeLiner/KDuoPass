rootProject.name = "KDuoPass"

pluginManagement {
    repositories {
        google {
            content { 
              	includeGroupByRegex("com\\.android.*")
              	includeGroupByRegex("com\\.google.*")
              	includeGroupByRegex("androidx.*")
              	includeGroupByRegex("android.*")
            }
        }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev/")
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content { 
              	includeGroupByRegex("com\\.android.*")
              	includeGroupByRegex("com\\.google.*")
              	includeGroupByRegex("androidx.*")
              	includeGroupByRegex("android.*")
            }
        }
        mavenCentral()
        maven("https://packages.jetbrains.team/maven/p/kpm/public")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev/")
    }
}
include(":composeApp")
includeBuild("buildConf")
