package com.neoutils.nil.decoder.gif.impl

import android.os.Build
import com.neoutils.nil.core.source.Decoder

class AndroidGifDecoder() : Decoder by when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
        AnimatedImageDecoder()
    }

    else -> {
        MovieGifDecoder()
    }
}
