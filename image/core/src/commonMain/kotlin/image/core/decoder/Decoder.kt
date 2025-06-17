package image.core.decoder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.painter.Painter

val LocalDecoders = compositionLocalOf<List<Decoder>> { listOf() }

@Composable
fun ProvideDecoders(
    vararg decoders: Decoder,
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalDecoders provides LocalDecoders.current + decoders,
    content = content
)

interface Decoder {
    fun decode(input: ByteArray): PainterProvider
    fun support(input: ByteArray): Support
}

interface PainterProvider {
    @Composable
    fun provide(): Painter
}

enum class Support {
    NONE,
    PARTIAL,
    TOTAL
}