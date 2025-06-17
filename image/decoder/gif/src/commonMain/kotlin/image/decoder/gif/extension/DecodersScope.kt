package image.decoder.gif.extension

import image.core.scope.DecodersScope
import image.decoder.gif.impl.GifDecoder

fun DecodersScope.gif() {
    decoders.add(GifDecoder())
}