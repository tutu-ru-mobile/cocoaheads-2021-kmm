plugins {
    id("org.jetbrains.gradle.apple.applePlugin") version JETBRAINS_APPLE_GRADLE_PLUGIN
}

apple {
    iosApp {
        productName = "refreshkmm"

        sceneDelegateClass = "SceneDelegate"
        launchStoryboard = "LaunchScreen"

        //productInfo["NSAppTransportSecurity"] = mapOf("NSAllowsArbitraryLoads" to true)
        //buildSettings.OTHER_LDFLAGS("")

        dependencies {
            implementation(project(":client-shared"))
            implementation(project(":serialized-data"))
        }
    }
}
