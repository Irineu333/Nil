package com.neoutils.nil.decoder.bitmap.extension

import com.neoutils.nil.core.extension.DecodersScope
import com.neoutils.nil.decoder.bitmap.impl.BitmapDecoder

fun DecodersScope.bitmap() {
    add(BitmapDecoder())
}
