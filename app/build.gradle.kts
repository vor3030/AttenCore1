// App-level build.gradle.kts
// Location: YourProject/app/build.gradle.kts

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "edu.cit.oliveros.attencore"
    compileSdk = 36

    defaultConfig {
        applicationId = "edu.cit.oliveros.attencore"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        compose = false
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ============= FACEBOOK SDK =============
    implementation("com.facebook.android:facebook-android-sdk:18.1.3")

    // ============= GOOGLE SIGN-IN =============
    implementation("com.google.android.gms:play-services-auth:21.5.1")
    implementation("com.google.android.gms:play-services-base:18.10.0")

    // ============= MICROSOFT MSAL =============
    implementation("com.microsoft.identity.client:msal:2.+") {
        exclude("com.microsoft.device.display", "display-mask")
    }



    // ============= FIREBASE (Apple Sign-In) =============
    implementation(platform("com.google.firebase:firebase-bom:34.10.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")

    // ============= RETROFIT & HTTP =============
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // ============= SECURITY =============
    implementation("androidx.security:security-crypto:1.1.0")
}