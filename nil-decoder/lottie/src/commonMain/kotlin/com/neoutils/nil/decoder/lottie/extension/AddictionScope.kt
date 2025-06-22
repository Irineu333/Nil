package com.neoutils.nil.decoder.lottie.extension

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.decoder.lottie.impl.LottieDecoder
import com.neoutils.nil.decoder.lottie.scope.LottieExtraScope

fun AddictionScope<Decoder<Extra>>.lottie() {
    add(LottieDecoder())
}

fun AddictionScope<Extra>.lottie(
    scope: LottieExtraScope.() -> Unit
) {
    add(LottieExtraScope().apply(scope).build())
}