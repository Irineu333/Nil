package image.decoder.xml.extension

import image.core.compose.DecodersScope
import image.decoder.xml.impl.XmlDecoder

fun DecodersScope.xml() {
    decoders.add(XmlDecoder())
}