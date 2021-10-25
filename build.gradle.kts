plugins {
    kotlin("plugin.serialization") version KOTLIN_VERSION apply false
    kotlin("multiplatform") version KOTLIN_VERSION apply false
    id("org.jetbrains.compose") version COMPOSE_DESKTOP apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$ANDROID_GRADLE_PLUGIN")
    }
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
