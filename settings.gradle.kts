@file:Suppress("UnstableApiUsage")

rootProject.name = "ImageLoader-POC"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")

include(":nil-core")
include(":nil-decoder:svg")
include(":nil-decoder:xml")
include(":nil-decoder:gif")
include(":nil-decoder:bitmap")
include(":nil-fetcher:network")
include(":nil-fetcher:resources")
include(":nil-interceptor:memory-cache")
include(":nil-interceptor:disk-cache")
include(":nil-util")
