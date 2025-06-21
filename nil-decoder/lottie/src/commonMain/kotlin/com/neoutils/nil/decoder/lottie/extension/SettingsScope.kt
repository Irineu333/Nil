package com.neoutils.nil.decoder.lottie.extension

import com.neoutils.nil.core.scope.SettingsScope
import com.neoutils.nil.decoder.lottie.scope.LottieScope

fun SettingsScope.lottie(
    scope: LottieScope.() -> Unit
) {
    params += LottieScope().apply(scope).build()
}