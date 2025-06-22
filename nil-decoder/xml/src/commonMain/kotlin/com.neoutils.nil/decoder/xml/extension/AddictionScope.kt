package com.neoutils.nil.decoder.xml.extension

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.decoder.xml.impl.XmlDecoder

fun AddictionScope<Decoder<Extra>>.xml() {
    add(XmlDecoder())
}