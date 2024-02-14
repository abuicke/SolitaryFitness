plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.gravitycode.solitaryfitnessapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gravitycode.solitaryfitnessapp"
        minSdk = 26
        // noinspection OldTargetApi
        targetSdk = 33
        versionCode = 6
        versionName = "1.0.0"
        multiDexEnabled = false
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "CRASH_ON_ERROR", "false")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("boolean", "CRASH_ON_ERROR", "false")
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

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Compose
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Multi Dex
    // implementation("androidx.multidex:multidex:2.0.1")

    // Navigation
    // implementation("androidx.navigation:navigation-runtime-ktx:2.7.6")

    // Dagger 2
    implementation("com.google.dagger:dagger:2.50")
    kapt("com.google.dagger:dagger-compiler:2.50")
    implementation("com.google.dagger:dagger-android:2.50")
    // if you use the support libraries
    // implementation("com.google.dagger:dagger-android-support:2.x")
    kapt("com.google.dagger:dagger-android-processor:2.50")

    // Data Store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Firebase
    // Use the BoM (Bill-of-Materials) so that only compatible versions of the Firebase libraries are
    // used, provided no explicit version is set on the libraries (https://reflectoring.io/maven-bom/)
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-firestore-ktx")
    // Not part of the BoM, so requires an explicit version
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Coil - Image Loading
    // implementation("io.coil-kt:coil:2.5.0")
    implementation("io.coil-kt:coil-compose:2.5.0")

    // WheelPickerCompose - https://github.com/commandiron/WheelPickerCompose
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")

    // kotlinx.collections.immutable - https://github.com/Kotlin/kotlinx.collections.immutable
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.6")

    // Leak Canary - enable when needed, as it conflicts with StrictMode
    debugImplementation("com.squareup.leakcanary:leakcanary-android:3.0-alpha-1")

    // Unit Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    // Optional -- Robolectric environment
    // testImplementation("androidx.test:core:1.5.0")
    // Optional -- Mockito framework
    // testImplementation("org.mockito:mockito-core:5.5.0")
    // Optional -- mockito-kotlin
    // testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
    // Optional -- Mockk framework
    // testImplementation("io.mockk:mockk:1.13.8")

    // Instrumentation Testing
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // noinspection GradleDependency - versions 1.7.0 -> 1.7.3 causes NoClassDefFoundError
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // UI Testing (Compose)
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // UI Testing (Espresso and UI Automator)
    // androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
}