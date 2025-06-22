package com.neoutils.nil.decoder.lottie.extension

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.scope.AddictionScope
import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.core.util.Extra
import com.neoutils.nil.decoder.lottie.impl.LottieDecoder
import com.neoutils.nil.decoder.lottie.scope.LottieScope
import kotlin.collections.plus

fun AddictionScope<Decoder<Extra>>.lottie() {
    add(LottieDecoder())
}

fun AddictionScope<Extra>.lottie(
    scope: LottieScope.() -> Unit
) {
    add(LottieScope().apply(scope).build())
}