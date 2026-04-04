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
    implementation("com.facebook.android:facebook-android-sdk:18.2.3")

    // ============= GOOGLE SIGN-IN (Credential Manager) =============
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.2.0")

    // Legacy Google Sign-In (Remove after full migration if not needed)
    // implementation("com.google.android.gms:play-services-auth:21.5.1")

    // ============= MICROSOFT MSAL =============
    implementation("com.microsoft.identity.client:msal:2.+") {
        exclude("com.microsoft.device.display", "display-mask")
    }

    // ============= FIREBASE (Apple Sign-In) =============
    implementation(platform("com.google.firebase:firebase-bom:34.11.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")

    // ============= RETROFIT & HTTP =============
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

    // ============= SECURITY =============
    implementation("androidx.security:security-crypto:1.1.0")
}
