package com.neoutils.nil.decoder.lottie.extension

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.scope.SettingScope
import com.neoutils.nil.decoder.lottie.impl.LottieDecoder

fun SettingScope<Decoder>.lottie() {
    add(LottieDecoder())
}