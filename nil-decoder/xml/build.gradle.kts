@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
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

            // module
            implementation(project(":nil-core"))

            // compose
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "com.neoutils.nil.decoder.xml"
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
        artifactId = "xml-decoder",
        groupId = "com.neoutils.nil",
        version = "0.1.0-alpha03"
    )

    configure(
        KotlinMultiplatform(
            sourcesJar = true,
            androidVariantsToPublish = listOf("release"),
        )
    )

    pom {
        name = "xml-decoder"
        description = "Drawable Vector decoder for Nil."
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
