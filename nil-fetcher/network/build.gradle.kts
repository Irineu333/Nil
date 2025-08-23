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

            // compose
            implementation(compose.runtime)
            implementation(libs.compose.ui.graphics)

            // ktor
            api(libs.ktor.client.core)

            // coroutines
            implementation(libs.kotlinx.coroutines)
        }
    }
}

