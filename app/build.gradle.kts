plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.gravitycode.solitaryfitness"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.gravitycode.solitaryfitness"
        minSdk = 26
        //noinspection OldTargetApi
        targetSdk = 33
        versionCode = 4
        versionName = "0.2.2"
        multiDexEnabled = false
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

    // Compose
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    // Multi Dex
    implementation("androidx.multidex:multidex:2.0.1")

    // Navigation
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.5")

    // Dagger 2
    implementation("com.google.dagger:dagger:2.48")
    kapt("com.google.dagger:dagger-compiler:2.48")
    implementation("com.google.dagger:dagger-android:2.48")
    // if you use the support libraries
    // implementation("com.google.dagger:dagger-android-support:2.x")
    kapt("com.google.dagger:dagger-android-processor:2.48")

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

    // JCIP Concurrency Annotations
    // implementation("net.jcip:jcip-annotations:1.0")

    // Klutter
    // implementation("uy.kohesive.klutter:klutter-core:3.0.0")

    // Guava
    implementation("com.google.guava:guava:32.1.2-android")

    // WheelPickerCompose - https://github.com/commandiron/WheelPickerCompose
    implementation("com.github.commandiron:WheelPickerCompose:1.1.11")

    // Tooling
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // JUnit
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