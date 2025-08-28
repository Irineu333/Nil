# Nil

Uma biblioteca de carregamento de imagens para Compose Multiplatform.

## Início rápido

Importe o módulo core, um fetcher e um decodificador para começar.

```kotlin
implementation("com.neoutils.nil:core:0.1.0-alpha04")
implementation("com.neoutils.nil:bitmap-decoder:0.1.0-alpha04")
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