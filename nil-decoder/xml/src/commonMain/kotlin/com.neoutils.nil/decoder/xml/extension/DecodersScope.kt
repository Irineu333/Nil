package com.neoutils.nil.decoder.xml.extension

import com.neoutils.nil.decoder.xml.impl.XmlDecoder
import com.neoutils.nil.core.scope.DecodersScope

fun DecodersScope.xml() {
    decoders.add(XmlDecoder())
}