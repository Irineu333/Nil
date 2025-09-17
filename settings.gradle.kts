@file:Suppress("UnstableApiUsage")

rootProject.name = "Nil"

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
include(":nil-fetcher:network-default")
include(":nil-fetcher:resources")
include(":nil-interceptor:memory-cache")
include(":nil-interceptor:disk-cache")
include(":nil-util")
