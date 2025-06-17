package image.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import image.core.decoder.Decoder
import kotlin.collections.plus

val LocalDecoders = compositionLocalOf<List<Decoder>> { listOf() }

@Composable
fun ProvideDecoders(
    vararg decoders: Decoder,
    content: @Composable () -> Unit
) = CompositionLocalProvider(
    LocalDecoders provides LocalDecoders.current + decoders,
    content = content
)
