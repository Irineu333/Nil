# Nil - Kotlin Multiplatform Image Loading Library

A powerful and flexible image loading library for Compose Multiplatform applications, supporting Android, iOS, and Desktop platforms.

## 🌟 Features

- **Multiplatform Support**: Works on Android, iOS, and Desktop
- **Compose Integration**: Native Compose support with `@Composable` functions
- **Async & Sync Loading**: Choose between asynchronous and synchronous image loading
- **Loading States**: Built-in support for loading, success, and error states with progress tracking
- **Multiple Image Formats**: Support for Bitmap, SVG, XML vectors, and GIF animations
- **Flexible Data Sources**: Load from network URLs, local resources, and more
- **Caching System**: Memory and disk caching with configurable strategies
- **Extensible Architecture**: Plugin-based system with interceptors, fetchers, and decoders
- **Placeholder & Fallback**: Customizable placeholder and fallback images

## 📦 Installation

Add the dependencies to your `build.gradle.kts`:

### Core Library
```kotlin
dependencies {
    implementation("com.neoutils.nil:core:0.1.0-alpha04")
}
```

### Image Decoders
```kotlin
dependencies {
    // For bitmap images (JPEG, PNG, etc.)
    implementation("com.neoutils.nil:bitmap-decoder:0.1.0-alpha04")
    
    // For SVG images
    implementation("com.neoutils.nil:svg-decoder:0.1.0-alpha04")
    
    // For XML vector drawables
    implementation("com.neoutils.nil:xml-decoder:0.1.0-alpha04")
    
    // For GIF animations
    implementation("com.neoutils.nil:gif-decoder:0.1.0-alpha04")
}
```

### Data Fetchers
```kotlin
dependencies {
    // For network requests
    implementation("com.neoutils.nil:network-fetcher:0.1.0-alpha04")
    
    // For local resources
    implementation("com.neoutils.nil:resources-fetcher:0.1.0-alpha04")
}
```

### Interceptors (Optional)
```kotlin
dependencies {
    // Memory caching
    implementation("com.neoutils.nil:memory-cache-interceptor:0.1.0-alpha04")
    
    // Disk caching
    implementation("com.neoutils.nil:disk-cache-interceptor:0.1.0-alpha04")
}
```

> **Note**: This library is currently in alpha. APIs may change between versions.

## 🏗️ Platform Setup

### Android
Add to your `androidMain` dependencies:
```kotlin
androidMain.dependencies {
    implementation(libs.ktor.client.android)
}
```

### iOS
Add to your `iosMain` dependencies:
```kotlin
iosMain.dependencies {
    implementation(libs.ktor.client.darwin)
}
```

### Desktop (JVM)
Add to your `desktopMain` dependencies:
```kotlin
val desktopMain by getting {
    dependencies {
        implementation(libs.ktor.client.java)
        implementation(libs.kotlinx.coroutines.swing)
    }
}
```

## 🚀 Quick Start

### Basic Network Image Loading

```kotlin
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.neoutils.nil.core.composable.asyncPainterResource
import com.neoutils.nil.core.contract.Request
import com.neoutils.nil.core.painter.PainterResource
import com.neoutils.nil.fetcher.network.extension.network

@Composable
fun NetworkImageExample() {
    val painter = asyncPainterResource(
        request = Request.network("https://example.com/image.jpg")
    )
    
    when (painter) {
        is PainterResource.Result.Success -> {
            Image(
                painter = painter,
                contentDescription = "Network Image",
                modifier = Modifier.fillMaxSize()
            )
        }
        is PainterResource.Loading -> {
            // Show loading indicator
            CircularProgressIndicator()
        }
        is PainterResource.Result.Failure -> {
            // Show error message
            Text("Failed to load image: ${painter.throwable.message}")
        }
    }
}
```

## ⚙️ Configuration

For testing with local resources, you can use `ResourcesPreview`:

```kotlin
@Composable
fun PreviewExample() {
    ResourcesPreview(
        settings = {
            fetchers {
                resources()
            }
            
            decoders {
                xml()
                bitmap()
                gif()
            }
        }
    ) {
        Image(
            painter = syncPainterResource(
                request = Request.resource(Res.drawable.atom_vector),
            ),
            contentDescription = null,
        )
    }
}
```

### Loading Local Resources

```kotlin
import org.jetbrains.compose.resources.DrawableResource
import com.neoutils.nil.fetcher.resources.extension.resource

@Composable
fun LocalResourceExample() {
    val painter = syncPainterResource(
        request = Request.resource(Res.drawable.my_image)
    )
    
    Image(
        painter = painter,
        contentDescription = "Local Resource"
    )
}
```

Configure the library with custom settings using the DSL:

```kotlin
@Composable
fun ConfiguredImageLoader() {
    ProvideSettings(
        settings = {
            // Configure fetchers
            fetchers {
                network() // Enable network fetching
                resources() // Enable resource fetching
            }
            
            // Configure decoders
            decoders {
                bitmap() // Support bitmap images
                svg() // Support SVG images
                xml() // Support XML vectors
                gif() // Support GIF animations
            }
            
            // Configure interceptors
            interceptors {
                memoryCache() // Enable memory caching
                diskCache() // Enable disk caching
            }
        }
    ) {
        // Your app content here
        MyApp()
    }
}
```

## 🎨 Advanced Usage

### Custom Placeholders and Fallbacks

```kotlin
@Composable
fun ImageWithPlaceholder() {
    val painter = asyncPainterResource(
        request = Request.network("https://example.com/image.jpg"),
        placeholder = painterResource(R.drawable.placeholder),
        fallback = painterResource(R.drawable.error_image)
    )
    
    Image(
        painter = painter,
        contentDescription = "Image with placeholder"
    )
}
```

### Loading with Progress

```kotlin
@Composable
fun ImageWithProgress() {
    val resource = asyncPainterResource(
        request = Request.network("https://example.com/large-image.jpg")
    )
    
    when (resource) {
        is PainterResource.Loading -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    progress = { resource.progress ?: 0f }
                )
                resource.progress?.let { progress ->
                    Text("Loading: ${(progress * 100).toInt()}%")
                }
            }
        }
        is PainterResource.Result.Success -> {
            Image(
                painter = resource,
                contentDescription = "Loaded image"
            )
        }
        is PainterResource.Result.Failure -> {
            Text("Error: ${resource.throwable.message}")
        }
    }
}
```

### Custom Network Configuration

```kotlin
@Composable
fun CustomNetworkImage() {
    val painter = asyncPainterResource(
        request = Request.network(
            url = "https://api.example.com/image",
            method = HttpMethod.Post,
            key = "custom-cache-key"
        )
    )
    
    // Use the painter...
}
```

## 🏗️ Architecture

The library follows a modular, extensible architecture:

### Core Components

- **Nil**: Main engine that orchestrates the image loading process
- **Request**: Defines what image to load and how
- **Chain**: Represents the processing pipeline
- **Settings**: Configuration for the library behavior

### Plugin System

- **Fetchers**: Retrieve data from various sources (network, resources, etc.)
- **Decoders**: Convert raw data into images (bitmap, SVG, GIF, etc.)
- **Interceptors**: Modify the loading process (caching, transformations, etc.)

### Data Flow

```
Request → Fetchers → Interceptors → Decoders → PainterResource
```

## 📖 API Reference

### Core Functions

#### `asyncPainterResource`
```kotlin
@Composable
fun asyncPainterResource(
    request: Request,
    placeholder: Painter = EmptyPainter,
    fallback: Painter = EmptyPainter,
    settings: SettingsScope.() -> Unit = {}
): PainterResource
```

Loads an image asynchronously with loading states.

#### `syncPainterResource`
```kotlin
@Composable
fun syncPainterResource(
    request: Request,
    fallback: Painter = EmptyPainter,
    settings: SettingsScope.() -> Unit = {}
): PainterResource
```

Loads an image synchronously (blocks until loaded).

### Request Types

#### Network Request
```kotlin
Request.network(
    url: String,
    method: HttpMethod = HttpMethod.Get,
    key: String = url
)
```

#### Resource Request
```kotlin
Request.resource(
    res: DrawableResource,
    environment: ResourceEnvironment = rememberResourceEnvironment()
)
```

### PainterResource States

- `PainterResource.Loading(progress: Float?)`: Image is loading
- `PainterResource.Result.Success`: Image loaded successfully
- `PainterResource.Result.Failure(throwable: Throwable)`: Loading failed

## 🔧 Supported Formats

- **Bitmap**: JPEG, PNG, WebP, BMP
- **Vector**: SVG, XML Vector Drawables
- **Animated**: GIF
- **Web**: Network images via HTTP/HTTPS

## 🚀 Performance

- **Memory Efficient**: Smart caching with configurable limits
- **Network Optimized**: Built on Ktor with connection pooling
- **UI Responsive**: Async loading keeps UI thread free
- **Progressive Loading**: Support for streaming and progress updates

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Irineu A. Silva** - [GitHub](https://github.com/Irineu333)

## 🔗 Dependencies

This library uses the following key dependencies:

- **Kotlin Multiplatform**: Cross-platform development
- **Jetpack Compose**: UI framework
- **Ktor**: HTTP client for network requests
- **Coroutines**: Asynchronous programming
- **Okio**: I/O operations

---

For more examples and detailed documentation, please check the example app in the `composeApp` module.