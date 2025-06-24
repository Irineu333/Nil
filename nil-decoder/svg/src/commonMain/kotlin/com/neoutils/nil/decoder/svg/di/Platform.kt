package com.neoutils.nil.decoder.svg.di

import androidx.compose.ui.unit.Density
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.util.Param

internal expect fun fromPlatform(density: Density): Decoder<Param>