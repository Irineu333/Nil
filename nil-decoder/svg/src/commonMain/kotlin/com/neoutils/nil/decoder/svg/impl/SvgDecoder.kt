package com.neoutils.nil.decoder.svg.impl

import androidx.compose.ui.unit.Density
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.Param
import com.neoutils.nil.decoder.svg.di.fromPlatform

class SvgDecoder(
    private val density: Density
) : Decoder<Param> by fromPlatform(density = density)