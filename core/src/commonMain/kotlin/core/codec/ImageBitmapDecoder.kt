package core.codec

import androidx.compose.ui.graphics.ImageBitmap
import org.jetbrains.compose.resources.decodeToImageBitmap

class ImageBitmapDecoder : Decoder<ByteArray, ImageBitmap> {
    override fun decode(input: ByteArray): ImageBitmap {
        return input.decodeToImageBitmap()
    }
}