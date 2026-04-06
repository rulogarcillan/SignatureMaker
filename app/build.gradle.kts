plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinComposeCompiler)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics.gradle)
    id("kotlin-parcelize")
}


android {
    namespace = libs.versions.namespace.get()
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.applicationId.get()
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        vectorDrawables {
            useSupportLibrary = true
        }
        versionCode = 4010
        versionName = "4.0.4"
    }


    signingConfigs {
        getByName("debug") {
            storeFile = file("androiddebug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            storeFile = file("xxxxx")
            storePassword = "xxxxx"
            keyAlias = "xxxxx"
            keyPassword = "xxxxx"
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            buildConfigField("String", "BASE_URL", "\"https://api.tuppersoft.com\"")
            buildConfigField("String", "END_POINT", "\"/commons\"")
            buildConfigField("String", "APP_NAME", "\"Signature maker\"")
            resValue("string", "app_name", "@string/app_name_debug")
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("release") {
            buildConfigField("String", "BASE_URL", "\"https://api.tuppersoft.com\"")
            buildConfigField("String", "END_POINT", "\"/commons\"")
            buildConfigField("String", "APP_NAME", "\"Signature maker\"")
            resValue("string", "app_name", "@string/app_name_release")
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }


    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(platform(libs.compose.bom))
    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Google Play Services - Ads
    implementation(libs.play.services.ads)

    // Google Play In-App Review
    implementation(libs.play.review)

    // Google Play In-App Update
    implementation(libs.play.app.update)

    // Google Play Services
    implementation(libs.user.messaging.platform)

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.browser)

    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.materialIconsExtended)
    implementation(libs.compose.runtime.livedata)

    api(libs.kotlin.collections.immutable)

    implementation (libs.koin.android)
    implementation (libs.koin.android.compose)

    implementation(libs.signature.pad)
    implementation(libs.uiTextGoogleFonts)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.loggingInterceptor)
    implementation(libs.converterMoshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    implementation(projects.skizoKotlinCore)
    implementation(projects.skizoAndroidCore)
    implementation(libs.coilCompose)

}
