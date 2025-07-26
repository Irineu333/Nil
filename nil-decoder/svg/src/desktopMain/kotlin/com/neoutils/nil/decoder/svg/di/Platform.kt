package com.neoutils.nil.decoder.svg.di

import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.decoder.svg.impl.SkiaSvgDecoder

internal actual val platformDecoder: Decoder get() = SkiaSvgDecoder()