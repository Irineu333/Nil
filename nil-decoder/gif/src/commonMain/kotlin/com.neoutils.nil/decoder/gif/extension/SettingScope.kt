package com.neoutils.nil.decoder.gif.extension

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.scope.SettingScope
import com.neoutils.nil.decoder.gif.impl.GifDecoder

fun SettingScope<Decoder>.gif() {
    add(GifDecoder())
}