package com.neoutils.nil.decoder.svg.impl

import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.decoder.svg.di.platformDecoder

// don't support common and android preview
class SvgDecoder() : Decoder by platformDecoder