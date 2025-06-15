package com.neoutils.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
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
        is Resource.Loading -> {
            if (resource.progress != null) {
                CircularProgressIndicator(progress = resource.progress)
            } else {
                CircularProgressIndicator()
            }
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
    data class Loading(
        val progress: Float? = null
    ) : Resource<Nothing>

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

fun Image.Animated.animate() = callbackFlow {

    val bitmap = Bitmap().apply {
        allocPixels(codec.imageInfo)
    }

    while (true) {
        repeat(times = codec.frameCount - 1) { index ->

            codec.readPixels(bitmap, index)

            withContext(Dispatchers.Main) {
                send(bitmap.asComposeImageBitmap())
            }

            delay(codec.framesInfo[index].duration.milliseconds)
        }
    }
}.flowOn(Dispatchers.Default)

class ImageLoader(
    private val client: HttpClient = HttpClient()
) {
    suspend fun get(url: String): Resource.Result<Image> {
        return try {
            val response = client.get(url)

            val image = withContext(Dispatchers.Default) {
                response.toImage()
            }

            Resource.Result.Success(image)
        } catch (e: Throwable) {
            Resource.Result.Failure(e)
        }
    }

    fun fetch(url: String) = channelFlow {
        try {
            val response = client.get(url) {
                onProgress { progress ->
                    withContext(Dispatchers.Main) {
                        send(Resource.Loading(progress))
                    }
                }
            }

            val image = withContext(Dispatchers.Default) {
                response.toImage()
            }

            withContext(Dispatchers.Main) {
                send(Resource.Result.Success(image))
            }
        } catch (e: Throwable) {
            withContext(Dispatchers.Main) {
                send(Resource.Result.Failure(e))
            }
        }
    }

    private fun HttpRequestBuilder.onProgress(
        update: suspend (progress: Float?) -> Unit
    ) = onDownload { bytesSentTotal, contentLength ->
        update(
            contentLength?.let {
                bytesSentTotal.toFloat()
                    .div(contentLength.toFloat())
                    .coerceIn(minimumValue = 0f, maximumValue = 1f)
                    .takeUnless { it.isNaN() }
            }
        )
    }

    private suspend fun HttpResponse.toImage(): Image {

        val bytes = bodyAsBytes()

        val data = Data.makeFromBytes(bytes)
        val codec = Codec.makeFromData(data)

        return if (codec.frameCount == 1) {
            Image.Static(
                bitmap = bytes.decodeToImageBitmap()
            )
        } else {
            Image.Animated(
                codec = codec,
            )
        }
    }
}

@Composable
fun asyncImageBitmapResource(url: String): Resource<ImageBitmap> {

    val imageLoader = remember { ImageLoader() }

    val imageFlow = remember(url) { imageLoader.fetch(url) }

    val resource by imageFlow.collectAsState(initial = Resource.Loading())

    return resource.mapSuccess {
        when (it) {
            is Image.Animated -> it.animatedImageBitmap()
            is Image.Static -> it.bitmap
        }
    }
}

@Composable
fun Image.Animated.animatedImageBitmap(): ImageBitmap {

    val animationFlow = remember { animate() }

    return animationFlow.collectAsState(
        initial = ImageBitmap(1, 1)
    ).value
}
