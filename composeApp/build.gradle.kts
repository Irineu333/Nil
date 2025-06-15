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

            // modules
            implementation(project(":core"))

            // compose
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.components.resources)

            // ktor
            implementation(libs.ktor.client.core)

            // coroutines
            implementation(libs.kotlinx.coroutines)
        }

        getByName("desktopMain").dependencies {

            // compose
            implementation(compose.desktop.currentOs)

            // ktor
            implementation(libs.ktor.client.okhttp)

            // slf4j
            implementation("org.slf4j:slf4j-nop:2.0.9")

            // coroutines
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.neoutils.image.MainKt"
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.neoutils.image.resources"
    generateResClass = always
}