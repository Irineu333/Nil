# Nil

An image loading library for Compose Multiplatform.

Translations: [Português](README-pt.md)

## Quick Start

Import the core module, a fetcher, and a decoder to get started.

```kotlin
implementation("com.neoutils.nil:core:0.1.0-alpha04")
implementation("com.neoutils.nil:bitmap-decoder:0.1.0-alpha04")
implementation("com.neoutils.nil:network-fetcher:0.1.0-alpha04")
```

The network fetcher uses **ktor3** internally and requires you to specify
a [ktor engine](https://ktor.io/docs/client-engines.html) for each platform.

```kotlin
androidMain {
    dependencies {
        implementation("io.ktor:ktor-client-android:3.1.0")
    }
}
appleMain {
    dependencies {
        implementation("io.ktor:ktor-client-darwin:3.1.0")
    }
}
jvmMain {
    dependencies {
        implementation("io.ktor:ktor-client-java:3.1.0")
    }
}
```

Use `asyncPainterResource` to load an image asynchronously.

```kotlin
Image(
    painter = asyncPainterResource(
        Request.network("https://cataas.com/cat")
    ),
    contentDescription = null,
)
```

## Cache

To enable memory cache and disk cache, just add the following dependencies.

```kotlin
implementation("com.neoutils.nil:memory-cache:0.1.0-alpha04")
implementation("com.neoutils.nil:disk-cache:0.1.0-alpha04")
```

Use the `memoryCache` and `diskCache` extensions to configure.

```kotlin
Image(
    painter = asyncPainterResource(
        Request.network("https://cataas.com/cat"),
    ) {
        extras {
            diskCache {
                maxSize = 10.mb
            }

            memoryCache {
                maxSize = 10
            }
        }
    },
    contentDescription = null,
)
```

## Translations
