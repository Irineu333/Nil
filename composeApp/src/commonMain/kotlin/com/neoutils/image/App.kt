package com.neoutils.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun App() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    val resource = asyncImageBitmapResource(url = "https://cataas.com/cat/gif")

    when (resource) {
        Resource.Loading -> {
            BasicText(text = "loading...")
        }

        is Resource.Result.Failure -> {
            BasicText(text = "error: ${resource.exception.message}")
        }

        is Resource.Result.Success<ImageBitmap> -> {
            Image(
                bitmap = resource.data,
                contentDescription = null
            )
        }
    }
}

sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>

    sealed interface Result<out T> : Resource<T> {

        data class Success<out T>(
            val data: T
        ) : Result<T>

        data class Failure(
            val exception: Throwable
        ) : Result<Nothing>
    }
}

inline fun <T, R> Resource<T>.mapSuccess(transform: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Loading -> this
        is Resource.Result.Failure -> this
        is Resource.Result.Success<T> -> Resource.Result.Success(transform(data))
    }
}

sealed interface Image {

    class Static(
        val bitmap: ImageBitmap
    ) : Image

    class Animated(
        val codec: Codec,
    ) : Image
}

fun Image.Animated.animate() = flow {

    val bitmap = Bitmap().apply {
        allocPixels(codec.imageInfo)
    }

    while (true) {
        repeat(times = codec.frameCount - 1) { index ->

            codec.readPixels(bitmap, index)

            emit(bitmap.asComposeImageBitmap())

            delay(codec.framesInfo[index].duration.milliseconds)
        }
    }
}

class ImageLoader(
    private val httpClient: HttpClient = HttpClient()
) {
    suspend fun get(url: String): Resource.Result<Image> {
        try {
            val bytes = httpClient.get(url).bodyAsBytes()

            val data = Data.makeFromBytes(bytes)
            val codec = Codec.makeFromData(data)

            val image = if (codec.frameCount == 1) {
                Image.Static(
                    bitmap = bytes.decodeToImageBitmap()
                )
            } else {
                Image.Animated(
                    codec = codec,
                )
            }

            return Resource.Result.Success(data = image)
        } catch (e: Throwable) {
            return Resource.Result.Failure(e)
        }
    }
}

@Composable
fun asyncImageBitmapResource(url: String): Resource<ImageBitmap> {

    val image = remember { mutableStateOf<Resource<Image>>(Resource.Loading) }

    val imageLoader = remember { ImageLoader() }

    LaunchedEffect(url) {
        image.value = imageLoader.get(url)
    }

    return image.value.mapSuccess {
        when (it) {
            is Image.Animated -> it.animatedImageBitmap()
            is Image.Static -> it.bitmap
        }
    }
}

@Composable
fun Image.Animated.animatedImageBitmap(): ImageBitmap {

    val animation = remember { animate() }

    return animation.collectAsState(
        initial = ImageBitmap(1, 1)
    ).value
}
