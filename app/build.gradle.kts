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
    implementation(libs.facebook.android.sdk)

    // ============= GOOGLE SIGN-IN (Credential Manager) =============
    implementation(libs.androidx.credentials.core)
    implementation(libs.androidx.credentials.play.auth)
    implementation(libs.googleid)

    // ============= FIREBASE =============
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.analytics)

    // ============= RETROFIT & HTTP =============
    implementation(libs.retrofit.lib)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.lib)
    implementation(libs.okhttp.logging.interceptor)

    // ============= SECURITY =============
    implementation(libs.androidx.security.crypto)
}
