package com.neoutils.nil.decoder.bitmap.extension

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.decoder.bitmap.impl.BitmapDecoder

fun AddictionScope<Decoder<Extra>>.bitmap() {
    add(BitmapDecoder())
}