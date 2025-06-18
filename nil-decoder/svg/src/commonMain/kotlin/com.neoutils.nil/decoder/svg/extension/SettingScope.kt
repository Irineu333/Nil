package com.neoutils.nil.decoder.svg.extension

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.scope.SettingScope
import com.neoutils.nil.decoder.svg.impl.SvgDecoder

fun SettingScope<Decoder>.svg() {
    add(SvgDecoder())
}