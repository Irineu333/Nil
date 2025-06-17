package image.decoder.bitmap.extension

import image.core.scope.DecodersScope
import image.decoder.bitmap.impl.BitmapDecoder

fun DecodersScope.bitmap() {
    decoders.add(BitmapDecoder())
}