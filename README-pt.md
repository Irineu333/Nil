# Nil

Uma biblioteca de carregamento de imagens para Compose Multiplatform.

## Início rápido

Importe o módulo core, um fetcher e um decodificador para começar.

```kotlin
implementation("com.neoutils.nil:core:0.1.0-alpha04")
implementation("com.neoutils.nil:bitmap-decoder:0.1.0-alpha04") // suporta PNG, JPEG e WEBP
implementation("com.neoutils.nil:network-fetcher:0.1.0-alpha04")
```

O fetcher de network utiliza o **ktor3** internamente, e requer que especifique
um [ktor engine](https://ktor.io/docs/client-engines.html) para cada
plataforma.

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

Para habilitar o cache em memória e o cache em disco, basta adicionar as seguintes dependências.

```kotlin
implementation("com.neoutils.nil:memory-cache:0.1.0-alpha04")
implementation("com.neoutils.nil:disk-cache:0.1.0-alpha04")
```

Utilize as extensões `memoryCache` e `diskCache` para configurar.

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

Utilize extras para ajustes.

```kotlin
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