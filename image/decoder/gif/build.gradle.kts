@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm("desktop")

    sourceSets {

        commonMain.dependencies {

            // module
            implementation(project(":image:core"))

            // compose
            implementation(compose.runtime)
            implementation(libs.compose.ui.graphics)

            // coroutines
            implementation(libs.kotlinx.coroutines)
        }
    }
}
