package com.neoutils.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import com.neoutils.image.resources.Res
import com.neoutils.image.resources.atom_vector
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
import org.jetbrains.compose.resources.*
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import org.jetbrains.skia.EncodedImageFormat
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun App() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    val resource = asyncPainterResource(res = Res.drawable.atom_vector)

    when (resource) {
        is Resource.Loading -> {
            if (resource.progress != null) {
                CircularProgressIndicator(progress = resource.progress)
            } else {
                CircularProgressIndicator()
            }
        }

        is Resource.Result.Failure -> {
            throw resource.exception
        }

        is Resource.Result.Success<Painter> -> {
            Image(
                painter = resource.data,
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
        val data: ByteArray
    ) : Image

    class Animated(
        val codec: Codec,
    ) : Image

    class Svg(
        val data: ByteArray
    ) : Image

    class Vector(
        val data: ByteArray
    ) : Image
}

private val DefaultAnimationDuration = 100.milliseconds

fun Image.Animated.animateImageBitmap() = callbackFlow {

    val bitmap = Bitmap().apply {
        allocPixels(codec.imageInfo)
    }

    while (true) {
        repeat(times = codec.frameCount - 1) { index ->

            codec.readPixels(bitmap, index)

            withContext(Dispatchers.Main) {
                send(bitmap.asComposeImageBitmap())
            }

            val frameDuration = codec.framesInfo[index].duration.milliseconds

            delay(frameDuration.takeOrElse { DefaultAnimationDuration })
        }
    }
}.flowOn(Dispatchers.Default)

private fun Duration.takeOrElse(block: () -> Duration): Duration {
    return if (absoluteValue == Duration.ZERO) block() else this
}

class ImageFromUrl(
    private val client: HttpClient = HttpClient()
) {
    suspend fun get(url: String): Resource.Result<Image> {
        return try {
            val response = client.get(url)

            val image = withContext(Dispatchers.Default) {
                response.bodyAsBytes().toImage()
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
                response.bodyAsBytes().toImage()
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
}

class ImageFromResource(
    val environment: ResourceEnvironment
) {
    suspend fun get(res: DrawableResource): Resource.Result<Image> {
        return try {
            val bytes = withContext(Dispatchers.IO) {
                getDrawableResourceBytes(environment, res)
            }

            val image = withContext(Dispatchers.Default) {
                bytes.toImage()
            }

            Resource.Result.Success(image)
        } catch (e: Exception) {
            Resource.Result.Failure(e)
        }
    }
}

val SVG_REGEX = Regex(pattern = "<svg[\\s\\S]+/>")
val VECTOR_REGEX = Regex(pattern = "<vector[\\s\\S]+>[\\s\\S]+</vector>")

fun ByteArray.toImage(): Image {

    val data = Data.makeFromBytes(this)

    runCatching {
        val codec = Codec.makeFromData(data)

        return when (codec.encodedImageFormat) {
            EncodedImageFormat.GIF -> {
                Image.Animated(codec = codec)
            }

            EncodedImageFormat.BMP,
            EncodedImageFormat.ICO,
            EncodedImageFormat.JPEG,
            EncodedImageFormat.PNG,
            EncodedImageFormat.WEBP -> {
                Image.Static(data = this)
            }

            EncodedImageFormat.WBMP -> null
            EncodedImageFormat.PKM -> null
            EncodedImageFormat.KTX -> null
            EncodedImageFormat.ASTC -> null
            EncodedImageFormat.DNG -> null
            EncodedImageFormat.HEIF -> null
        } ?: return@runCatching
    }

    val content = decodeToString()

    if (content.contains(SVG_REGEX)) {
        return Image.Svg(data = this)
    }
    if (content.contains(VECTOR_REGEX)) {
        return Image.Vector(data = this)
    }

    error("Unsupported format")
}

@Composable
fun asyncPainterResource(
    url: String,
    imageFromUrl: ImageFromUrl = remember { ImageFromUrl() }
): Resource<Painter> {

    val imageResourceFlow = remember(imageFromUrl, url) { imageFromUrl.fetch(url) }

    val imageResource by imageResourceFlow.collectAsState(initial = Resource.Loading())

    return imageResource.mapSuccess { it.resolveAsPainter() }
}

@Composable
fun rememberImageFromResource(
    environment: ResourceEnvironment = rememberResourceEnvironment()
) = remember(environment) { ImageFromResource(environment) }

@Composable
fun asyncPainterResource(
    res: DrawableResource,
    imageFromResource: ImageFromResource = rememberImageFromResource()
): Resource<Painter> {

    var imageResource by remember { mutableStateOf<Resource<Image>>(Resource.Loading()) }

    LaunchedEffect(imageFromResource, res) {
        imageResource = imageFromResource.get(res)
    }

    return imageResource.mapSuccess { it.resolveAsPainter() }
}

@Composable
fun Image.resolveAsPainter(): Painter {

    val density = LocalDensity.current

    return when (this) {
        is Image.Animated -> BitmapPainter(animatedImageBitmap())
        is Image.Static -> BitmapPainter(data.decodeToImageBitmap())
        is Image.Svg -> data.decodeToSvgPainter(density)
        is Image.Vector -> rememberVectorPainter(data.decodeToImageVector(density))
    }
}

private val ImageBlank = ImageBitmap(1, 1)

@Composable
fun Image.Animated.animatedImageBitmap(): ImageBitmap {

    val animationFlow = remember(codec) { animateImageBitmap() }

    return animationFlow.collectAsState(
        initial = ImageBlank
    ).value
}
