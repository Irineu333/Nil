package image.decoder.xml.extension

import image.core.scope.DecodersScope
import image.decoder.xml.impl.XmlDecoder

fun DecodersScope.xml() {
    decoders.add(XmlDecoder())
}