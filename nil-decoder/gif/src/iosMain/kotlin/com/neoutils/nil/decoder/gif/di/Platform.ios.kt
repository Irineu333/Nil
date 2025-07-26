package com.neoutils.nil.decoder.gif.di

import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.decoder.gif.impl.SkiaGifDecoder

internal actual val platformDecoder : Decoder get() = SkiaGifDecoder()
