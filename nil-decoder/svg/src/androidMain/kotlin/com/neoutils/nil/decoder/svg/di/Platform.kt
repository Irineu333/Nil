package com.neoutils.nil.decoder.svg.di

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.decoder.svg.impl.AndroidSvgDecoder

internal actual val platformDecoder: Decoder get() = AndroidSvgDecoder()