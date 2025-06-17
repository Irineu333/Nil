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

include(":image:core")
include(":image:decoder:svg")
include(":image:decoder:xml")
include(":image:decoder:gif")
include(":image:decoder:bitmap")
include(":image:fetcher:network")
include(":image:fetcher:resources")