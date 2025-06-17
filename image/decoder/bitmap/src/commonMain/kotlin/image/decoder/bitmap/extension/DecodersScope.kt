package image.decoder.bitmap.extension

import image.core.compose.DecodersScope
import image.decoder.bitmap.impl.BitmapDecoder

fun DecodersScope.bitmap() {
    decoders.add(BitmapDecoder())
}