plugins {
    id("org.jetbrains.gradle.apple.applePlugin") version JETBRAINS_APPLE_GRADLE_PLUGIN
}

apple {
    iosApp {
        productName = "refreshkmm"

        sceneDelegateClass = "SceneDelegate"
        launchStoryboard = "LaunchScreen"

        // Нужно для работы http://localhost
        productInfo["NSAppTransportSecurity"] = mapOf("NSAllowsArbitraryLoads" to true)
        //buildSettings.OTHER_LDFLAGS("")

        dependencies {
            implementation(project(":shared"))
        }
    }
}
