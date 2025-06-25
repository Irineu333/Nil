package com.neoutils.nil.decoder.svg.di

import androidx.compose.ui.unit.Density
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.decoder.svg.impl.DesktopSvgDecoder

internal actual fun fromPlatform(
    density: Density
): Decoder<Any> = DesktopSvgDecoder(
    density = density
)