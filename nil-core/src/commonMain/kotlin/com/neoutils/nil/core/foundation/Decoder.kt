package com.neoutils.nil.core.foundation

import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.util.Dynamic
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support

val DecodersExtra = Extras.Key(Dynamic.decoders)

interface Decoder {
    suspend fun decode(
        input: ByteArray,
        extras: Extras = Extras.EMPTY
    ): Resource.Result<Painter>

    suspend fun support(input: ByteArray): Support
}
