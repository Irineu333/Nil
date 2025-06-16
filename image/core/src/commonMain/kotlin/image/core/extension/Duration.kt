package image.core.extension

import kotlin.time.Duration

fun Duration.takeOrElse(block: () -> Duration): Duration {
    return if (absoluteValue == Duration.ZERO) block() else this
}