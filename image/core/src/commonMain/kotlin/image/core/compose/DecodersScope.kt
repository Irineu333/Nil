package image.core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import image.core.decoder.Decoder

val LocalDecoders = compositionLocalOf<List<Decoder>> { listOf() }

data class DecodersScope(
    val decoders: MutableList<Decoder> = mutableListOf()
) {
    fun build() = decoders.toList()

    companion object {
        @Composable
        fun fromLocal(): DecodersScope {

            val decoders = LocalDecoders.current

            return remember(decoders) { DecodersScope(decoders.toMutableList()) }
        }
    }
}
