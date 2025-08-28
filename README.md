# Nil

An image loading library for Compose Multiplatform.

Translations: [Português](README-pt.md)

## Quick Start

Import the core module, a fetcher, and a decoder to get started.

```kotlin
implementation("com.neoutils.nil:core:0.1.0-alpha04")
implementation("com.neoutils.nil:bitmap-decoder:0.1.0-alpha04") // supports PNG, JPEG and WEBP
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

``` kotlin
Image(
    painter = asyncPainterResource(...) {
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

## GIF Support

For animated image support, add the GIF decoder dependency.

```kotlin
implementation("com.neoutils.nil:gif-decoder:0.1.0-alpha04")
```

And declare it in the configuration.

```kotlin
Image(
    painter = asyncPainterResource(
        Request.network("https://cataas.com/cat/gif"),
    ) {
        decoders {
            gif() // or add(GifDecoder())
        }
    },
    contentDescription = null,
)
```

Use extras for adjustments.

``` kotlin
Image(
    painter = asyncPainterResource(...) {
        ...
    
        extras {
            gif {
                repeatCount = 2
            }
        }
    },
    contentDescription = null,
)
```

## SVG Support

For SVG image support, add the SVG decoder dependency.

```kotlin
implementation("com.neoutils.nil:svg-decoder:0.1.0-alpha04")
```

And declare it in the configuration.

```kotlin
Image(
    painter = asyncPainterResource(
        Request.network("https://example.com/image.svg"),
    ) {
        decoders {
            svg() // or add(SvgDecoder())
        }
    },
    contentDescription = null,
)
```

## Compose Resources

To load images from Compose resources, add the following dependency.

```kotlin
implementation("com.neoutils.nil:resources-fetcher:0.1.0-alpha04")
```

And use the `Request.resource(...)` extension.

```kotlin
Image(
    painter = syncPainterResource(
        Request.resource(Res.drawable.cute_cat),
    ),
    contentDescription = null,
)
```

### XML Support

For Drawable Image Vector support, add the XML decoder dependency.

```kotlin
implementation("com.neoutils.nil:xml-decoder:0.1.0-alpha04")
```

And declare it in the configuration.

```kotlin
Image(
    painter = syncPainterResource(
        Request.resource(Res.drawable.vector_icon),
    ) {
        decoders {
            xml() // or add(XmlDecoder())
        }
    },
    contentDescription = null,
)
```
