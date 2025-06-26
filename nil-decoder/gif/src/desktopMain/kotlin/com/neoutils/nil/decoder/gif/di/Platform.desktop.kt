package com.neoutils.nil.decoder.gif.di

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.decoder.gif.impl.SkiaGifDecoder
import com.neoutils.nil.decoder.gif.model.GifParams

internal actual val platformDecoder: Decoder get() = SkiaGifDecoder()
