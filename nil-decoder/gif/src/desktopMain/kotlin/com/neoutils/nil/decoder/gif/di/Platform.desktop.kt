package com.neoutils.nil.decoder.gif.di

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.decoder.gif.impl.SkiaGifDecoder
import com.neoutils.nil.decoder.gif.model.GifParam

internal actual val platformDecoder: Decoder<GifParam> get() = SkiaGifDecoder()
