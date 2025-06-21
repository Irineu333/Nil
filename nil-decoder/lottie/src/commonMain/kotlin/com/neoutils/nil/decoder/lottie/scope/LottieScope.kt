package com.neoutils.nil.decoder.lottie.scope

import com.neoutils.nil.decoder.lottie.model.LottieParams

class LottieScope(
    var iterations: Int? = null,
    var speed: Float? = null
) {
    internal fun build() = LottieParams(
        iterations = iterations,
        speed = speed
    )
}