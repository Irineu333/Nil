package com.neoutils.nil.decoder.bitmap.extension

import com.neoutils.nil.decoder.bitmap.impl.BitmapDecoder
import com.neoutils.nil.core.scope.DecodersScope

fun DecodersScope.bitmap() {
    decoders.add(BitmapDecoder())
}