package com.neoutils.nil.decoder.xml.extension

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.scope.SettingScope
import com.neoutils.nil.decoder.xml.impl.XmlDecoder

fun SettingScope<Decoder>.xml() {
    add(XmlDecoder())
}