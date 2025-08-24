package com.neoutils.nil.decoder.xml.impl

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.exception.NotSupportFormat
import com.neoutils.nil.core.constant.DensityExtrasKey
import com.neoutils.nil.core.extension.resourceCatching
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.xml.painter.VectorPainter
import org.jetbrains.compose.resources.decodeToImageVector

private val VECTOR_REGEX = Regex(pattern = "<vector[\\s\\S]+>[\\s\\S]+</vector>")

class XmlDecoder() : Decoder {

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ):  Resource.Result<Painter> {

        if (support(input) == Support.NONE) {
            return  Resource.Result.Failure(NotSupportFormat())
        }

        return resourceCatching {

            val density = extras[DensityExtrasKey]

            VectorPainter(input.decodeToImageVector(density), density)
        }
    }

    override suspend fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(VECTOR_REGEX)) {
            return Support.RECOMMEND
        }

        return Support.NONE
    }
}
