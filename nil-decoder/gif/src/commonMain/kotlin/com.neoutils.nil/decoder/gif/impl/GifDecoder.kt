package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.decoder.gif.model.GifParams

internal expect val platformDecoder: Decoder<GifParams>

class GifDecoder() : Decoder<GifParams> by platformDecoder