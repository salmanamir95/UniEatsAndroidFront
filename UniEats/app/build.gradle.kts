import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
}

android {
    val localProperties = Properties().apply {
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            load(localPropertiesFile.inputStream())
        } else {
            throw GradleException("local.properties file not found!")
        }
    }
    namespace = "com.example.unieats"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.unieats"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        val clientId = localProperties.getProperty("defaultWebClientId")

        if (clientId.isNullOrBlank()) {
            throw GradleException("defaultWebClientId is not defined in local.properties")
        } else {
            resValue("string", "default_web_client_id", clientId)
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth.ktx)

    // Google Sign-In
    implementation ("com.google.android.gms:play-services-auth:21.0.0")

    // Room
    implementation(libs.androidx.room.runtime.android)

    implementation(libs.firebase.storage)

}
