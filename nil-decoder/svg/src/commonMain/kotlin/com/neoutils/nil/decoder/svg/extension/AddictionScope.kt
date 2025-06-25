package com.neoutils.nil.decoder.svg.extension

import androidx.compose.ui.unit.Density
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.decoder.svg.impl.SvgDecoder

fun AddictionScope<Decoder<*>>.svg(density: Density) {
    add(SvgDecoder(density = density))
}