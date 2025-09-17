package com.neoutils.nil.decoder.xml.extension

import com.neoutils.nil.core.extension.DecodersScope
import com.neoutils.nil.decoder.xml.impl.XmlDecoder

fun DecodersScope.xml() {
    add(XmlDecoder())
}
