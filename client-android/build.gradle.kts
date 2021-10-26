plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = ANDROID_COMPILE_SDK
    defaultConfig {
        applicationId = "ru.tutu.androidApp"
        minSdk = ANDROID_MIN_SDK
        targetSdk = ANDROID_TARGET_SDK
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = COMPOSE_ANDROID
    }
}

dependencies {
    // Material design icons
    implementation("androidx.compose.ui:ui:$COMPOSE_ANDROID")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:$COMPOSE_ANDROID")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:$COMPOSE_ANDROID")
    // Material Design
    implementation("androidx.compose.material:material:$COMPOSE_ANDROID")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.activity:activity-ktx:1.3.1")
    implementation("androidx.activity:activity-compose:1.3.1")
    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINE_VERSION")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINE_VERSION")

}
