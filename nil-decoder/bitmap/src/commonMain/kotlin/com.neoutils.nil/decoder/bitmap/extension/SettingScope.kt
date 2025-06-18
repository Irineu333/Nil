package com.neoutils.nil.decoder.bitmap.extension

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.scope.SettingScope
import com.neoutils.nil.decoder.bitmap.impl.BitmapDecoder

fun SettingScope<Decoder>.bitmap() {
    add(BitmapDecoder())
}