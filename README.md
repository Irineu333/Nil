# Nil

An image loading library for **Compose Multiplatform**.

Translations: [Português](README-pt.md)

## Quick Start

Import the **core** module, a **fetcher**, and a **decoder** to get started.

```kotlin
implementation("com.neoutils.nil:core:0.1.0-alpha05")
implementation("com.neoutils.nil:bitmap-decoder:0.1.0-alpha05")
implementation("com.neoutils.nil:network-fetcher-default:0.1.0-alpha05")
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

Add the dependencies for the desired cache functionalities.

```kotlin
implementation("com.neoutils.nil:memory-cache:0.1.0-alpha05")
implementation("com.neoutils.nil:disk-cache:0.1.0-alpha05")
```

Use the `diskCache` and `memoryCache` extensions to configure.

``` kotlin
Image(
    painter = asyncPainterResource(...) {
        diskCache {
            maxSize = 10.mb
        }
    },
    contentDescription = null,
)
```

## GIF Support

For animated image support like **GIF** and **WebP**, add the `GifDecoder` dependency.

```kotlin
implementation("com.neoutils.nil:gif-decoder:0.1.0-alpha05")
```

And declare it in the configuration.

```kotlin
Image(
    painter = asyncPainterResource(
        Request.network("https://cataas.com/cat/gif"),
    ) {
        decoders {
            gif() // or +GifDecoder()
        }
    },
    contentDescription = null,
)
```

Use the `gif` extension for configuration.

``` kotlin
Image(
    painter = asyncPainterResource(...) {
        ...

        gif {
            repeatCount = 2
        }
    },
    contentDescription = null,
)
```

## SVG Support

For SVG image support, add the SVG decoder dependency.

```kotlin
implementation("com.neoutils.nil:svg-decoder:0.1.0-alpha05")
```

And declare it in the configuration.

```kotlin
Image(
    painter = asyncPainterResource(
        Request.network("https://example.com/image.svg"),
    ) {
        decoders {
            svg() // or +SvgDecoder()
        }
    },
    contentDescription = null,
)
```

## Compose Resources

To load images from Compose resources, add the following dependency.

```kotlin
implementation("com.neoutils.nil:resources-fetcher:0.1.0-alpha05")
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
implementation("com.neoutils.nil:xml-decoder:0.1.0-alpha05")
```

And declare it in the configuration.

```kotlin
Image(
    painter = syncPainterResource(
        Request.resource(Res.drawable.vector_icon),
    ) {
        decoders {
            xml() // or +XmlDecoder()
        }
    },
    contentDescription = null,
)
```

## Network Configuration

Use the `network` extension to configure the `NetworkFetcher`.

``` kotlin
Image(
    painter = asyncPainterResource(
        Request.network(...),
    ) {
        network {
            headers = mapOf("Authorization" to "Bearer ...")
        }
    },
    contentDescription = null,
)
```

Use the `network-fetcher` dependency instead of `network-fetcher-default` to manually specify
the [Ktor Client](https://ktor.io/docs/client-engines.html).

```kotlin
implementation("com.neoutils.nil:network-fetcher:0.1.0-alpha05")
```

Configure the client for each target.

``` kotlin
kotlin {
    sourceSets {
        androidMain {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:3.1.0")
            }
        }
        
        iosMain {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:3.1.0")
            }
        }
        
        jvmMain {
            dependencies {
                implementation("io.ktor:ktor-client-java:3.1.0")
            }
        }
        
        ...
    }
}

```

## State Handling

Use the `placeholder` and `failure` parameters to display images during loading or in case of failure.

``` kotlin
Image(
    painter = asyncPainterResource(
        request = Request.network("..."),
        placeholder = painterResource(Res.drawable.placeholder),
        failure = painterResource(Res.drawable.failure),
    ),
    contentDescription = null,
)
```

Or handle states manually.

``` kotlin
val resource = asyncPainterResource(
    Request.network("..."),
)

when (resource) {
    is PainterResource.Result.Success -> {
        Image(
            painter = resource,
            contentDescription = null,
        )
    }

    is PainterResource.Loading -> {
        if (resource.progress != null) {
            CircularProgressIndicator(
                progress = { resource.progress!! }
            )
        } else {
            CircularProgressIndicator()
        }
    }

    is PainterResource.Result.Failure -> {
        if (resource.throwable is ResponseException) {
            Image(
                painter = painterResource(Res.drawable.failure),
                contentDescription = null,
            )
        } else {
            throw resource.throwable
        }
    }
}
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
