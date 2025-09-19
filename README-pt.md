# Nil

Uma biblioteca de carregamento de imagens para **Compose Multiplatform**.

## Início rápido

Importe o módulo **core**, um **fetcher** e um **decodificador**.

```kotlin
implementation("com.neoutils.nil:core:0.1.0-alpha05")
implementation("com.neoutils.nil:bitmap-decoder:0.1.0-alpha05")
implementation("com.neoutils.nil:network-fetcher-default:0.1.0-alpha05")
```

Utilize `asyncPainterResource` para carregar uma imagem de forma assíncrona.

```kotlin
Image(
    painter = asyncPainterResource(
        Request.network("https://cataas.com/cat")
    ),
    contentDescription = null,
)
```

## Cache

Adicione as dependencias das funcionalidades de cache desejadas.

```kotlin
implementation("com.neoutils.nil:memory-cache:0.1.0-alpha05")
implementation("com.neoutils.nil:disk-cache:0.1.0-alpha05")
```

Utilize as extensões `diskCache` e `memoryCache` para configurar.

``` kotlin
Image(
    painter = asyncPainterResource(...) {
        diskCache {
            maxSize = 10.mb
            ...
        }
    },
    contentDescription = null,
)
```

## Suporte a GIF

Para suporte a imagens animadas como **Gif** e **WebP**, adicione a dependencia do `GifDecoder`.

```kotlin
implementation("com.neoutils.nil:gif-decoder:0.1.0-alpha05")
```

E declare na configuração.

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

Utilize a extensão `gif` para configurar.

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

## Suporte a SVG

Para suporte a imagens **SVG**, adicione a dependência do `SvgDecoder`.

```kotlin
implementation("com.neoutils.nil:svg-decoder:0.1.0-alpha05")
```

E declare na configuração.

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

Para carregar imagens do **Compose Resources**, adicione a dependencia de `ResourcesFetcher`.

```kotlin
implementation("com.neoutils.nil:resources-fetcher:0.1.0-alpha05")
```

E utilize a extension `Request.resource(...)` para acionar.

```kotlin
Image(
    painter = syncPainterResource(
        Request.resource(Res.drawable.cute_cat),
    ),
    contentDescription = null,
)
```

### Suporte a XML

Para exibir icones do android, `DrawableImageVector`, adicione a dependência do `XmlDecoder`.

```kotlin
implementation("com.neoutils.nil:xml-decoder:0.1.0-alpha05")
```

E declare na configuração.

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

## Configurações de rede

Utilize a extensão `network` para configurar o `NetworkFetcher`.

``` kotlin
Image(
    painter = asyncPainterResource(
        Request.network(...),
    ) {
        network {
            headers = mapOf("Autorization" to "Bearer ...")
        }
    },
    contentDescription = null,
)
```

Utilize a dependência do `network-fetcher` invés `network-fetcher-default` para especificar manualmente
o [Ktor Client](https://ktor.io/docs/client-engines.html).

```kotlin
implementation("com.neoutils.nil:network-fetcher:0.1.0-alpha05")
```

Configure o client para cada salvo.

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

## Tratamento de estados

Utilize os parâmetros `placeholder` e `failure` para exibir imagens durante o carregamento ou em caso de falha.

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

Ou trate os estados manualmente.

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
