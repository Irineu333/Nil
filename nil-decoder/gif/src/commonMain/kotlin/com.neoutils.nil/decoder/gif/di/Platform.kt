package com.neoutils.nil.decoder.gif.di

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.decoder.gif.model.GifParams

internal expect val platformDecoder: Decoder<GifParams>
