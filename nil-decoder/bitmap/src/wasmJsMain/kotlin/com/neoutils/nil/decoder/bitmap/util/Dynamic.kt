@file:OptIn(ExperimentalStdlibApi::class)

package com.neoutils.nil.decoder.bitmap.util

import com.neoutils.nil.core.util.decoders
import com.neoutils.nil.decoder.bitmap.impl.BitmapDecoder

@EagerInitialization
private val initHook = run {
    decoders.add(BitmapDecoder())
}