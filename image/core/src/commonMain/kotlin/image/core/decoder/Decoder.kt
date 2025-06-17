package image.core.decoder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import image.core.provider.PainterProvider
import image.core.util.Support


interface Decoder {
    fun decode(input: ByteArray): PainterProvider
    fun support(input: ByteArray): Support
}
