# Nil

Uma biblioteca de carregamento de imagens para Compose Multiplatform.

## Início rápido

Importe o módulo core, um fetcher e um decodificador para começar.

```kotlin
implementation("com.neoutils.nil:core:0.1.0-alpha04")
implementation("com.neoutils.nil:bitmap-decoder:0.1.0-alpha04") // suporta PNG, JPEG e WEBP
implementation("com.neoutils.nil:network-fetcher-default:0.1.0-alpha04")
```

E utilize `asyncPainterResource` para carregar uma imagem de forma assíncrona.

```kotlin
Image(
    painter = asyncPainterResource(
        Request.network("https://cataas.com/cat")
    ),
    contentDescription = null,
)
```

## Cache

Para habilitar o cache em memória e o cache em disco, basta adicionar as seguintes dependências.

```kotlin
implementation("com.neoutils.nil:memory-cache:0.1.0-alpha04")
implementation("com.neoutils.nil:disk-cache:0.1.0-alpha04")
```

E utilize as extensões `memoryCache` e `diskCache` para configurar.

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

## Autenticação

Utilize a extensão network de extras para configurar autenticação básica.

``` kotlin
Image(
    painter = asyncPainterResource(
        Request.network(...),
    ) {
        extras {
            network {
                headers = mapOf("Bearer" to "...")
            }
        }
    },
    contentDescription = null,
)
```

## Suporte a GIF

Para suporte a imagens animadas, adicione a dependência do decodificador de GIF.

```kotlin
implementation("com.neoutils.nil:gif-decoder:0.1.0-alpha04")
```

E declare na configuração.

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

Utilize a extensão `gif` para configurar.

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

## Suporte a SVG

Para suporte a imagens SVG, adicione a dependência do decodificador de SVG.

```kotlin
implementation("com.neoutils.nil:svg-decoder:0.1.0-alpha04")
```

E declare na configuração.

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

Para carregar imagens dos recursos do Compose, adicione a seguinte dependência.

```kotlin
implementation("com.neoutils.nil:resources-fetcher:0.1.0-alpha04")
```

E utilize a extension `Request.resource(...)` para carregar.

```kotlin
Image(
    painter = syncPainterResource(
        Request.resource(Res.drawable.cute_cat),
    ),
    contentDescription = null,
)
```

### Suporte a XML

Se precisa carregar um Drawable Image Vector dos recursos, adicione a dependência do decodificador de XML.

```kotlin
implementation("com.neoutils.nil:xml-decoder:0.1.0-alpha04")
```

E declare na configuração.

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