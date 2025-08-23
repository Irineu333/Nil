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

    applyDefaultHierarchyTemplate()

    sourceSets {

        commonMain {
            dependencies {

                // module
                implementation(project(":nil-core"))
                implementation(project(":nil-type"))

                // compose
                implementation(compose.runtime)
                implementation(compose.ui)

                // coroutines
                implementation(libs.kotlinx.coroutines)

                // lru cache
                implementation(libs.cache4k)
            }
        }

        val nonAndroidMain by creating {
            dependsOn(commonMain.get())
        }

        val desktopMain by getting {
            dependsOn(nonAndroidMain)
        }

        iosMain {
            dependsOn(nonAndroidMain)
        }

        val wasmJsMain by getting {
            dependsOn(nonAndroidMain)
        }

        all {
            languageSettings.enableLanguageFeature("WhenGuards")
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpected-actual-classes")
    }
}
