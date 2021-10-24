plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

kotlin {
    jvm()
    android()

    listOf(
        iosX64(),
        iosArm64()
        //iosSimulatorArm64() waiting ktor M1 support
    ).forEach {
        it.binaries.framework {
            baseName = "SerializedData"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$SERIALIZATION_VERSION")
            }
        }

        val androidMain by getting {}

        val iosX64Main by getting {}
        val iosArm64Main by getting {}
        //val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            //iosSimulatorArm64Main.dependsOn(this)
            dependencies {

            }
        }
    }
}

android {
    compileSdk = ANDROID_COMPILE_SDK//(findProperty("android.compileSdk") as String).toInt()

    defaultConfig {
        minSdk = ANDROID_MIN_SDK//(findProperty("android.minSdk") as String).toInt()
        targetSdk = ANDROID_TARGET_SDK//(findProperty("android.targetSdk") as String).toInt()
    }
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
