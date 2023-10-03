plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace = "com.gravitycode.simpletracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gravitycode.simpletracker"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        // multiDexEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // noinspection GradleDependency
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Multi Dex
    // implementation("androidx.multidex:multidex:2.0.1")

    // Navigation
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.3")

    // Dagger 2
    implementation("com.google.dagger:dagger:2.48")
    kapt("com.google.dagger:dagger-compiler:2.48")
    implementation("com.google.dagger:dagger-android:2.48")
    // if you use the support libraries
    // implementation("com.google.dagger:dagger-android-support:2.x")
    kapt("com.google.dagger:dagger-android-processor:2.48")

    // Data Store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Tooling
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // JUnit
    testImplementation("junit:junit:4.13.2")

    // Android Instrumentation Testing
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // Kotlin Coroutines
    // noinspection GradleDependency - versions 1.7.0 -> 1.7.3 causes NoClassDefFoundError
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    // I don't know what this is, there's another another compose library specified in
    // https://developer.android.com/training/testing/instrumented-tests, which is below
    // androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    // Optional -- UI testing with Espresso
    // androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Optional -- UI testing with UI Automator
    // androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    // Optional -- UI testing with Compose
    // androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}