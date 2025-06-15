package com.neoutils.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.neoutils.image.resources.Res
import com.neoutils.image.resources.atom
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
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.rememberResourceEnvironment
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import org.jetbrains.skia.Rect
import org.jetbrains.skia.svg.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun App() = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.fillMaxSize()
) {
    val resource = asyncPainterResource(res = Res.drawable.atom)

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
        val bitmap: ImageBitmap
    ) : Image

    class Animated(
        val codec: Codec,
    ) : Image

    class Vector(
        val svgDom: SVGDOM
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

class ImageFromResources(
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

fun ByteArray.toImage(): Image {

    val data = Data.makeFromBytes(this)

    runCatching {
        Image.Vector(SVGDOM(data))
    }.onSuccess {
        return it
    }

    val codec = Codec.makeFromData(data)

    return if (codec.frameCount == 1) {
        Image.Static(
            bitmap = decodeToImageBitmap()
        )
    } else {
        Image.Animated(
            codec = codec,
        )
    }
}

@Composable
fun asyncPainterResource(
    url: String,
    imageFromUrl: ImageFromUrl = remember { ImageFromUrl() }
): Resource<Painter> {

    val imageFlow = remember(imageFromUrl, url) { imageFromUrl.fetch(url) }

    val resource by imageFlow.collectAsState(initial = Resource.Loading())

    return resource.mapSuccess {
        when (it) {
            is Image.Animated -> BitmapPainter(it.animatedImageBitmap())
            is Image.Static -> BitmapPainter(it.bitmap)
            is Image.Vector -> SvgPainter(it.svgDom, LocalDensity.current)
        }
    }
}

@Composable
fun rememberImageFromResources(
    environment: ResourceEnvironment = rememberResourceEnvironment()
) = remember(environment) { ImageFromResources(environment) }

@Composable
fun asyncPainterResource(
    res: DrawableResource,
    imageFromResources: ImageFromResources = rememberImageFromResources()
): Resource<Painter> {

    var imageResource by remember { mutableStateOf<Resource<Image>>(Resource.Loading()) }

    LaunchedEffect(imageFromResources, res) {
        imageResource = imageFromResources.get(res)
    }

    return imageResource.mapSuccess {
        when (it) {
            is Image.Animated -> BitmapPainter(it.animatedImageBitmap())
            is Image.Static -> BitmapPainter(it.bitmap)
            is Image.Vector -> SvgPainter(it.svgDom, LocalDensity.current)
        }
    }
}

private val ImageBlank = ImageBitmap(1, 1)

@Composable
fun Image.Animated.animatedImageBitmap(): ImageBitmap {

    val animationFlow = remember { animateImageBitmap() }

    return animationFlow.collectAsState(
        initial = ImageBlank
    ).value
}

/**
 * Copied from Compose Resources
 */
class SvgPainter(
    private val dom: SVGDOM,
    private val density: Density
) : Painter() {
    private val svg = dom.root

    private val defaultSizePx: Size = run {
        val width = svg?.width?.withUnit(SVGLengthUnit.PX)?.value ?: 0f
        val height = svg?.height?.withUnit(SVGLengthUnit.PX)?.value ?: 0f
        if (width == 0f && height == 0f) {
            Size.Unspecified
        } else {
            Size(width, height)
        }
    }

    init {
        if (svg?.viewBox == null && defaultSizePx.isSpecified) {
            svg?.viewBox = Rect.makeXYWH(0f, 0f, defaultSizePx.width, defaultSizePx.height)
        }
    }

    override val intrinsicSize: Size
        get() {
            return if (defaultSizePx.isSpecified) {
                defaultSizePx * density.density
            } else {
                Size.Unspecified
            }
        }

    override fun DrawScope.onDraw() {
        drawIntoCanvas { canvas ->
            svg?.width = SVGLength(size.width, SVGLengthUnit.PX)
            svg?.height = SVGLength(size.height, SVGLengthUnit.PX)
            svg?.preserveAspectRatio = SVGPreserveAspectRatio(SVGPreserveAspectRatioAlign.NONE)
            dom.render(canvas.nativeCanvas)
        }
    }
}
