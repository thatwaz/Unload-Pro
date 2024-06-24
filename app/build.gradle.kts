plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.serialization")
}



android {
    namespace = "com.thatwaz.unloadpro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.thatwaz.unloadpro"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.database)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.compose.livedata)
//    implementation(libs.firebase.bom)
//    implementation(libs.firebase.firestore)

    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.firestore.ktx)
    implementation (libs.firebase.database)

    implementation("com.google.dagger:hilt-android:2.50")
    kapt(libs.hilt.android.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation ("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation ("androidx.compose.material3:material3-adaptive-navigation-suite:1.0.0-alpha07")
    implementation ("androidx.core:core-ktx:1.8.0")
    implementation ("androidx.compose.ui:ui:1.3.0")

    implementation ("androidx.navigation:navigation-compose:2.5.3")

    implementation ("androidx.compose.runtime:runtime-livedata:1.6.7")

    androidTestImplementation ("androidx.compose.ui:ui-test-junit4-android:1.6.7:1.0")
    debugImplementation ("androidx.compose.ui:ui-test-manifest")

    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // Hilt testing dependencies
    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.44")
    kaptAndroidTest ("com.google.dagger:hilt-compiler:2.50")

    // Compose UI testing dependencies
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.6.7")
    debugImplementation ("androidx.compose.ui:ui-test-manifest")

    // Coroutines test dependencies
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")

}