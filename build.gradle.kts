// Project-level build.gradle.kts
// Location: YourProject/build.gradle.kts (at ROOT, not in app folder)

plugins {
    id("com.android.application") version "9.0.1" apply false
    id("org.jetbrains.kotlin.android") version "2.3.10" apply false
}

 buildscript {
    dependencies {
         classpath("com.google.gms:google-services:4.4.4")
     }
     repositories {
         mavenCentral()
     }
 }
