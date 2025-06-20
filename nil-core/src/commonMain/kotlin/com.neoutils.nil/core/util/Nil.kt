@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.util

import com.neoutils.nil.core.decoder.Decoder
import com.neoutils.nil.core.extension.NoDecoderFound
import com.neoutils.nil.core.extension.combine
import com.neoutils.nil.core.fetcher.Fetcher
import com.neoutils.nil.core.fetcher.NoFetcherFound
import com.neoutils.nil.core.model.Settings
import com.neoutils.nil.core.painter.NilPainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

class Nil(
    internal val settings: Settings
) {
    fun execute(input: Input): Flow<Resource<NilPainter>> {
        return flow {
            when (val resource = getFetcher(input)) {
                is Resource.Loading -> emit(resource)
                is Resource.Result.Failure -> emit(resource)
                is Resource.Result.Success<Fetcher<Input>> -> {
                    emitAll(resource.data.fetch(input))
                }
            }
        }.mapLatest { dataRes ->
            dataRes.combine { data ->
                withContext(Dispatchers.Default) {
                    getDecoder(data).combine { decoder ->
                        decoder.decode(data)
                    }
                }
            }
        }
    }
}

fun Nil.getFetcher(input: Input): Resource<Fetcher<Input>> {

    if (settings.fetchers.isEmpty()) {
        return Resource.Result.Failure(NoFetcherFound())
    }

    val fetcher = settings.fetchers.find { it.type == input::class }

    return if (fetcher != null) {
        Resource.Result.Success(fetcher)
    } else {
        Resource.Result.Failure(NoFetcherFound("No supported fetcher found"))
    }
}

suspend fun Nil.getDecoder(bytes: ByteArray): Resource<Decoder> {

    if (settings.decoders.isEmpty()) {
        return Resource.Result.Failure(NoDecoderFound())
    }

    val decoder = settings.decoders.maxBy { it.support(bytes) }

    if (decoder.support(bytes) == Support.NONE) {
        return Resource.Result.Failure(NoDecoderFound("No supported decoder found"))
    }

    return Resource.Result.Success(decoder)
}
