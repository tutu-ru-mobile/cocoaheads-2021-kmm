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
    composeOptions {
        kotlinCompilerExtensionVersion = COMPOSE_ANDROID
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")

    implementation("androidx.compose.ui:ui:$COMPOSE_ANDROID")
    implementation("androidx.compose.ui:ui-tooling:$COMPOSE_ANDROID")
    implementation("androidx.compose.foundation:foundation:$COMPOSE_ANDROID")
    implementation("androidx.compose.material:material:$COMPOSE_ANDROID")
    implementation("androidx.activity:activity-compose:$COMPOSE_ANDROID")
    //Compose Utils
    implementation("io.coil-kt:coil-compose:1.4.0")
    implementation("com.google.accompanist:accompanist-insets:0.20.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.20.0")
    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINE_VERSION")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINE_VERSION")
    //DI
    implementation("io.insert-koin:koin-core:3.1.2")
    implementation("io.insert-koin:koin-android:3.1.2")
    //Navigation
    implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-beta13")
    //WorkManager
    implementation("androidx.work:work-runtime-ktx:2.7.0")
}
