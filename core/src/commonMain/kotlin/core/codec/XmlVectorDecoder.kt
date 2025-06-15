package core.codec

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Density
import org.jetbrains.compose.resources.decodeToImageVector

class XmlVectorDecoder(
    private val density: Density
) : Decoder<ByteArray, ImageVector> {
    override fun decode(input: ByteArray): ImageVector {
        return input.decodeToImageVector(density)
    }
}