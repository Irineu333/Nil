package image.decoder.svg.extension

import image.core.compose.DecodersScope
import image.decoder.svg.impl.SvgDecoder

fun DecodersScope.svg() {
    decoders.add(SvgDecoder())
}