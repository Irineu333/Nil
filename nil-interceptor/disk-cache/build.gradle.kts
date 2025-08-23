@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {

    jvm("desktop")

    wasmJs {
        browser()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {

            // module
            implementation(project(":nil-core"))
            implementation(project(":nil-util"))

            // compose
            implementation(compose.runtime)
            implementation(compose.ui)

            // coroutines
            implementation(libs.kotlinx.coroutines)

            // okio
            api(libs.okio)
        }
        all {
            languageSettings.enableLanguageFeature("WhenGuards")
        }
    }
}

