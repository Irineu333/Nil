@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {

            // maven
            implementation("com.neoutils.nil:core:0.1.0-alpha01")
            implementation("com.neoutils.nil:bitmap-decoder:0.1.0-alpha01")
            implementation("com.neoutils.nil:network-fetcher:0.1.0-alpha01")

            // compose
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // coroutines
            implementation(libs.kotlinx.coroutines)
        }

        androidMain.dependencies {

            // activity
            implementation(libs.androidx.activity.compose)

            // ktor
            implementation(libs.ktor.client.android)
        }

        val desktopMain by getting {
            dependencies {

                // compose
                implementation(compose.desktop.currentOs)

                // ktor
                implementation(libs.ktor.client.java)

                // coroutines
                implementation(libs.kotlinx.coroutines.swing)

                // slf4j
                implementation("org.slf4j:slf4j-nop:2.0.9")
            }
        }

        iosMain.dependencies {

            // ktor
            implementation(libs.ktor.client.darwin)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.neoutils.nil.example.MainKt"
    }
}

android {
    namespace = "com.neoutils.nil.example"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.neoutils.nil.example"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.androidx.ui.tooling)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.neoutils.nil.example.resources"
    generateResClass = always
}