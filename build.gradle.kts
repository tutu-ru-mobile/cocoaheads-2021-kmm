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
        classpath("com.android.tools.build:gradle:4.0.2")
    }
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
