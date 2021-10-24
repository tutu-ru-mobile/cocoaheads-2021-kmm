buildscript {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.20")
        classpath("com.android.tools.build:gradle:4.0.2")
    }
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
