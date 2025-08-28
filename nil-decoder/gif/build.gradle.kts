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

    applyDefaultHierarchyTemplate()

    sourceSets {

        commonMain {
            dependencies {

                // module
                implementation(project(":nil-core"))
                implementation(project(":nil-util"))

                // compose
                implementation(compose.runtime)
                implementation(compose.ui)

                // coroutines
                implementation(libs.kotlinx.coroutines)

                // lru cache
                implementation(libs.cache4k)
            }
        }

        androidMain {
            dependencies {

                // androidx
                implementation(libs.androidx.core)
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

        all {
            languageSettings.enableLanguageFeature("WhenGuards")
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

android {
    namespace = "com.neoutils.nil.decoder.gif"
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
        artifactId = "gif-decoder",
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
        name = "gif-decoder"
        description = "GIF decoder for Nil."
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
