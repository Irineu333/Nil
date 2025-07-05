package com.neoutils.nil.decoder.xml.extension

import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.decoder.xml.impl.XmlDecoder

fun ListScope<Decoder>.xml() {
    add(XmlDecoder())
}
