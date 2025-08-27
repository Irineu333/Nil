@file:OptIn(ExperimentalWasmDsl::class)

import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.maven.publish)
}

kotlin {

    jvm("desktop")

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {

        commonMain.dependencies {

            // io
            implementation(libs.okio)
        }

        all {
            languageSettings.enableLanguageFeature("WhenGuards")
        }
    }
}

android {
    namespace = "com.neoutils.nil.util"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
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

mavenPublishing {

    publishToMavenCentral()

    coordinates(
        artifactId = "util",
        groupId = "com.neoutils.nil",
        version = "0.1.0-alpha02"
    )

    configure(
        KotlinMultiplatform(
            sourcesJar = true,
            androidVariantsToPublish = listOf("release"),
        )
    )

    pom {
        name = "util"
        description = "Utilities for Nil."
        inceptionYear = "2025"
        url = "https://github.com/Irineu333/ImageLoader-POC"

        licenses {
            license {
                name = "The MIT License"
                url = "https://opensource.org/licenses/MIT"
            }
        }

        developers {
            developer {
                id = "irineu333"
                name = "Irineu A. Silva"
                url = "https://github.com/Irineu333"
            }
        }

        scm {
            url = "https://github.com/Irineu333/ImageLoader-POC"
            connection = "scm:git:git://github.com/Irineu333/ImageLoader-POC.git"
            developerConnection = "scm:git:ssh://git@github.com/Irineu333/ImageLoader-POC.git"
        }
    }

    signAllPublications()
}
