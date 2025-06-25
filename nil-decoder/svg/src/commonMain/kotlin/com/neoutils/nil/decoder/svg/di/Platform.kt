package com.neoutils.nil.decoder.svg.di

import androidx.compose.ui.unit.Density
import com.neoutils.nil.core.source.Decoder

internal expect fun fromPlatform(density: Density): Decoder<Any>