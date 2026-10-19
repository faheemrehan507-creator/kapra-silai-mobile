plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.stitchtech.kaprasilai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.stitchtech.kaprasilai.v4"
        minSdk = 24
        targetSdk = 34
        versionCode = 40
        versionName = "4.0.0"
    }

    signingConfigs {
        getByName("debug") {
            val ksFile = file("debug.keystore")
            if (ksFile.exists()) {
                storeFile = ksFile
                storePassword = "android"
                keyAlias = "androiddebugkey"
                keyPassword = "android"
            }
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
}
