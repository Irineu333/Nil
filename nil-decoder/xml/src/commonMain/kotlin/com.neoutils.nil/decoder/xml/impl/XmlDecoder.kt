package com.neoutils.nil.decoder.xml.impl

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.painter.NilPainter
import com.neoutils.nil.core.exception.NotSupportException
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.xml.painter.NilXmlPainter

private val VECTOR_REGEX = Regex(pattern = "<vector[\\s\\S]+>[\\s\\S]+</vector>")

class XmlDecoder() : Decoder {

    override suspend fun decode(input: ByteArray): Resource.Result<NilPainter> {
        return when (support(input)) {
            Support.NONE -> Resource.Result.Failure(NotSupportException())
            else -> Resource.Result.Success(NilXmlPainter(input))
        }
    }

    override fun support(input: ByteArray): Support {

        val content = input.decodeToString()

        if (content.contains(VECTOR_REGEX)) {
            return Support.TOTAL
        }

        return Support.NONE
    }
}
