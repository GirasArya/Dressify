// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.1" apply false

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    //ksp for room compiler
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
}