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
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
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

            // modules
            implementation(project(":nil-core"))
            implementation(project(":nil-fetcher:resources"))
            implementation(project(":nil-fetcher:network"))
            implementation(project(":nil-decoder:bitmap"))
            implementation(project(":nil-decoder:xml"))
            implementation(project(":nil-decoder:svg"))
            implementation(project(":nil-decoder:gif"))
            implementation(project(":nil-interceptor:disk-cache"))
            implementation(project(":nil-interceptor:memory-cache"))

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

        val wasmJsMain by getting {
            dependencies {

                // ktor
                implementation(libs.ktor.client.js)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.neoutils.nil.example.MainKt"
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.neoutils.nil.example.resources"
    generateResClass = always
}