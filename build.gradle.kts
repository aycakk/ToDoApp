// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id ("org.jetbrains.kotlin.plugin.serialization")version "2.0.21"

}
buildscript{
    dependencies{
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        classpath ("com.android.tools.build:gradle:8.5.2")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.0")

            classpath("com.google.gms:google-services:4.4.0")



    }
}