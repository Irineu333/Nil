package com.neoutils.nil.decoder.gif.impl

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.decoder.gif.di.platformDecoder
import com.neoutils.nil.decoder.gif.model.GifParams

class GifDecoder() : Decoder<GifParams> by platformDecoder