import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlinKapt)
    id("kotlin-parcelize")
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}



dependencies {
    implementation(libs.androidxCoreKtx)
    implementation(libs.androidxAppcompat)
    implementation(libs.androidxActivity)
    implementation(libs.androidxConstraintlayout)
    implementation(libs.androidxFragmentKtx)
    implementation(libs.androidxNavigationFragmentKtx)
    implementation(libs.androidxNavigationUiKtx)
    implementation(libs.androidxLifecycleViewmodelKtx)
    implementation(libs.androidxLifecycleExtensions)

    implementation(libs.googleMaterial)
    implementation(libs.androidxMaterial3)

    implementation(platform(libs.firebaseBom))
    implementation(libs.firebaseAnalytics)
    implementation(libs.firebaseDatabase)
    implementation(libs.firebaseFirestore)
    implementation(libs.firebaseAuth)
    implementation(libs.firebaseStorage)
    implementation(libs.firebaseAppcheckDebug)

    implementation(libs.playServicesAuth)

    implementation(libs.androidxRoomRuntime)
    kapt(libs.androidxRoomCompiler)

    implementation(libs.hiltAndroid)
    kapt(libs.hiltCompiler)

    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)

    implementation(libs.media3Common)

    implementation(libs.glide)
    implementation(libs.cloudinary)
}
