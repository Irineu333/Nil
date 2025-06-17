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

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {

            // modules
            implementation(project(":image:core"))
            implementation(project(":image:fetcher:resources"))
            implementation(project(":image:fetcher:network"))
            implementation(project(":image:decoder:bitmap"))
            implementation(project(":image:decoder:gif"))
            implementation(project(":image:decoder:svg"))
            implementation(project(":image:decoder:xml"))

            // compose
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)

            // coroutines
            implementation(libs.kotlinx.coroutines)
        }

        androidMain.dependencies {

            // activity
            implementation(libs.androidx.activity.compose)

            // ktor
            implementation(libs.ktor.client.android)
        }

        desktopMain.dependencies {

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
}

compose.desktop {
    application {
        mainClass = "com.neoutils.image.MainKt"
    }
}

android {
    namespace = "com.neoutils.image"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.neoutils.image"
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

compose.resources {
    publicResClass = true
    packageOfResClass = "com.neoutils.image.resources"
    generateResClass = always
}