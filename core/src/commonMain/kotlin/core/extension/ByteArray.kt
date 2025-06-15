package core.extension

import core.util.RawImage
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import org.jetbrains.skia.EncodedImageFormat

private val SVG_REGEX = Regex(pattern = "<svg[\\s\\S]+/>")
private val VECTOR_REGEX = Regex(pattern = "<vector[\\s\\S]+>[\\s\\S]+</vector>")

fun ByteArray.toRawImage(): RawImage {

    val data = Data.makeFromBytes(this)

    runCatching {
        val codec = Codec.makeFromData(data)

        return when (codec.encodedImageFormat) {
            EncodedImageFormat.GIF -> {
                RawImage.Animated(
                    codec = codec,
                    data = this
                )
            }

            EncodedImageFormat.BMP,
            EncodedImageFormat.ICO,
            EncodedImageFormat.JPEG,
            EncodedImageFormat.PNG,
            EncodedImageFormat.WEBP -> {
                RawImage.Static(
                    codec = codec,
                    data = this
                )
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
        return RawImage.Svg(data = this)
    }
    if (content.contains(VECTOR_REGEX)) {
        return RawImage.Vector(data = this)
    }

    error("Unsupported format")
}