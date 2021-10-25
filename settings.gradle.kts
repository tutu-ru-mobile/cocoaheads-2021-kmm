pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    
}
rootProject.name = "refresh-kmm"


include(":client-ios")
include(":client-android")
include(":client-shared")
include(":serialized-data")
include(":server")
include(":desktop")
