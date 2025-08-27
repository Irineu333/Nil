package com.neoutils.nil.decoder.gif.impl

import android.graphics.Movie
import androidx.compose.ui.graphics.painter.Painter
import com.neoutils.nil.core.exception.NotSupportFormat
import com.neoutils.nil.core.extension.resourceCatching
import com.neoutils.nil.core.foundation.Decoder
import com.neoutils.nil.core.util.Extras
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.core.util.Support
import com.neoutils.nil.decoder.gif.model.GifExtra
import com.neoutils.nil.decoder.gif.painter.MovieGifPainter
import com.neoutils.nil.util.Type

@Suppress("DEPRECATION")
class MovieGifDecoder : Decoder {

    override suspend fun decode(
        input: ByteArray,
        extras: Extras
    ): Resource.Result<Painter> {

        if (support(input) == Support.NONE) {
            return Resource.Result.Failure(NotSupportFormat())
        }

        return resourceCatching {
            val params = extras[GifExtra.ExtrasKey]

            MovieGifPainter(
                movie = Movie.decodeStream(input.inputStream()),
                repeatCount = params.repeatCount
            )
        }
    }

    override suspend fun support(input: ByteArray): Support {

        if (input.isEmpty()) return Support.NONE

        return when (Type.detect(input)) {
            Type.GIF -> Support.RECOMMEND
            else -> Support.NONE
        }
    }
}
