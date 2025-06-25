package com.neoutils.nil.decoder.xml.extension

import androidx.compose.ui.unit.Density
import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.Param
import com.neoutils.nil.decoder.xml.impl.XmlDecoder

fun AddictionScope<Decoder<Param>>.xml(density: Density) {
    add(XmlDecoder(density = density))
}