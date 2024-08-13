plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.youverifyassessment"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.youverifyassessment"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }
    dataBinding {
        enable = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    ndkVersion = "24.0.8215888"
    externalNativeBuild {
        ndkBuild {
            path = file("src/main/jni/Android.mk")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Coroutines, Retrofit, Hilt, Paging3, DPrefs, Logger
    implementation(libs.coroutines.dependency)
    implementation(libs.retrofit.dependency)
    implementation(libs.gson.converter)
    implementation(libs.dagger.hilt.dependency)
    implementation(libs.paging3.dependency)
    implementation(libs.nav.fragment)
    implementation(libs.nav.ui)
    implementation(libs.logging.interceptor.dependency)
    implementation(libs.dprefs.library)
    implementation(libs.coroutine.call.adapter)
    // Room
    implementation(libs.androidx.room.ktx)
    kapt(libs.room.annotation.processor.dependency)
    implementation(libs.room.dependency)
    implementation(libs.life.cycle)
    implementation(libs.paging3.room)
    implementation(libs.room.paging.dependency)

    // DataStore
    implementation(libs.datastore.dependency)

    implementation(libs.coil.dependency)
    implementation(libs.view.model.kts)
    implementation(libs.view.model.save.state.instance)
    implementation(libs.activity.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.fragment.nav)
    implementation(libs.ui.nav)
    implementation(libs.lottie.animation.view)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    kapt(libs.dagger.hilt.kapt)
    // Firebase
    implementation(platform(libs.firebase.dependency))
    implementation(libs.google.play.services.dependency)
}