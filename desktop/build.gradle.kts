import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform") // kotlin("jvm") doesn't work well in IDEA/AndroidStudio (https://github.com/JetBrains/compose-jb/issues/22)
    id("org.jetbrains.compose")
}

kotlin {
    jvm()
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":shared"))
                implementation(project(":serialized-data"))
                implementation(project(":server"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "ru.tutu.MainKt"
    }
}
