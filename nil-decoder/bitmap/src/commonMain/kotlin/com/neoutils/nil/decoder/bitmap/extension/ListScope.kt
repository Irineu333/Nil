package com.neoutils.nil.decoder.bitmap.extension

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.scope.ListScope
import com.neoutils.nil.decoder.bitmap.impl.BitmapDecoder

fun ListScope<Decoder>.bitmap() {
    add(BitmapDecoder())
}