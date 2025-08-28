@file:OptIn(ExperimentalWasmDsl::class)

import com.vanniktech.maven.publish.KotlinMultiplatform
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
}

kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }

    androidTarget {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
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
            implementation(compose.components.resources)
        }
    }
}

android {
    namespace = "com.neoutils.nil.decoder.bitmap"
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
        artifactId = "bitmap-decoder",
        groupId = "com.neoutils.nil",
        version = libs.versions.version.get(),
    )

    configure(
        KotlinMultiplatform(
            sourcesJar = true,
            androidVariantsToPublish = listOf("release"),
        )
    )

    pom {
        name = "bitmap-decoder"
        description = "PNG, JPEG and WEBP decoder for Nil."
        inceptionYear = "2025"
        url = "https://github.com/Irineu333/Nil"

        licenses {
            license {
                name = "Apache License, Version 2.0"
                url = "https://opensource.org/license/apache-2-0"
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
            url = "https://github.com/Irineu333/Nil"
            connection = "scm:git:git://github.com/Irineu333/Nil.git"
            developerConnection = "scm:git:ssh://git@github.com/Irineu333/Nil.git"
        }
    }

    signAllPublications()
}
