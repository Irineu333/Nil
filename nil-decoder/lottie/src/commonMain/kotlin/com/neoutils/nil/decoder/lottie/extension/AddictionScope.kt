package com.neoutils.nil.decoder.lottie.extension

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.util.Params
import com.neoutils.nil.decoder.lottie.impl.LottieDecoder

fun AddictionScope<Decoder<Params>>.lottie() {
    add(LottieDecoder())
}
