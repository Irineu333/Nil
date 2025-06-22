@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.extension.decoderFor
import com.neoutils.nil.core.extension.fetcherFor
import com.neoutils.nil.core.extension.paramsFor
import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Input
import com.neoutils.nil.core.util.PainterResource
import com.neoutils.nil.core.util.Params
import com.neoutils.nil.core.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest

class Nil(
    internal val settings: Settings
) {

    fun fetch(input: Input): Flow<Resource<ByteArray>> = flow {
        when (val resource = settings.fetchers.fetcherFor(input)) {
            is Resource.Result.Failure -> {
                emit(Resource.Result.Failure(resource.throwable))
            }

            is Resource.Result.Success<Fetcher<Input>> -> {
                val fetcher = resource.data
                emitAll(fetcher.fetch(input))
            }
        }
    }

    suspend fun decode(bytes: ByteArray): PainterResource.Result {
        return when (val decoder = settings.decoders.decoderFor(bytes)) {
            is Resource.Result.Failure -> {
                PainterResource.Result.Failure(decoder.throwable)
            }

            is Resource.Result.Success<Decoder<Params>> -> {
                val decoder = decoder.data
                val params = settings.params.paramsFor(decoder)

                decoder.decode(
                    input = bytes,
                    params = params,
                )
            }
        }
    }

    fun execute(input: Input) = fetch(input).mapLatest { resource ->
        when (resource) {
            is Resource.Loading -> {
                PainterResource.Loading(resource.progress)
            }

            is Resource.Result.Failure -> {
                PainterResource.Result.Failure(resource.throwable)
            }

            is Resource.Result.Success<ByteArray> -> {
                decode(bytes = resource.data)
            }
        }
    }
}

