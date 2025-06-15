package core.util

import org.jetbrains.skia.Codec

sealed interface RawImage {

    val data: ByteArray

    class Static(
        override val data: ByteArray,
        val codec: Codec,
    ) : RawImage

    class Animated(
        override val data: ByteArray,
        val codec: Codec,
    ) : RawImage

    class Svg(
        override val data: ByteArray
    ) : RawImage

    class Vector(
        override val data: ByteArray
    ) : RawImage
}
