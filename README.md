# Nil

An image loading library for Compose Multiplatform.

Translations: [Português](README-pt.md)

## Quick Start

Import the core module, a fetcher, and a decoder to get started.

```kotlin
implementation("com.neoutils.nil:core:0.1.0-alpha04")
implementation("com.neoutils.nil:bitmap-decoder:0.1.0-alpha04") // supports PNG, JPEG and WEBP
implementation("com.neoutils.nil:network-fetcher-default:0.1.0-alpha04")
```

And use `asyncPainterResource` to load an image asynchronously.

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

And use the `memoryCache` and `diskCache` extensions to configure.

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

## Authentication

Use the network extension in extras to configure basic authentication.

``` kotlin
Image(
    painter = asyncPainterResource(
        Request.network(...),
    ) {
        extras {
            network {
                headers = mapOf("Autorization" to "Bearer ...")
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

## License
```
Copyright 2025 Irineu A. Silva

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```