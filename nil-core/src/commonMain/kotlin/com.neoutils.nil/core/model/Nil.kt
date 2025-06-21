@file:OptIn(ExperimentalCoroutinesApi::class)

package com.neoutils.nil.core.model

import com.neoutils.nil.core.source.Decoder
import com.neoutils.nil.core.extension.decoderFor
import com.neoutils.nil.core.extension.fetcherFor
import com.neoutils.nil.core.extension.paramsFor
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
    fun execute(input: Input): Flow<PainterResource> = flow {
        when (val fetcher = settings.fetchers.fetcherFor(input)) {
            is Resource.Result.Failure -> emit(fetcher)
            is Resource.Result.Success<Fetcher<Input>> -> {
                emitAll(fetcher.data.fetch(input))
            }
        }
    }.mapLatest { output ->
        when (output) {
            is Resource.Loading -> {
                PainterResource.Loading(output.progress)
            }

            is Resource.Result.Failure -> {
                PainterResource.Result.Failure(output.throwable)
            }

            is Resource.Result.Success<ByteArray> -> {
                when (val decoder = settings.decoders.decoderFor(output.data)) {
                    is Resource.Result.Failure -> {
                        PainterResource.Result.Failure(decoder.throwable)
                    }

                    is Resource.Result.Success<Decoder<Params>> -> {

                        val decoder = decoder.data

                        decoder.decode(
                            input = output.data,
                            params = settings.params.paramsFor(decoder),
                        )
                    }
                }
            }
        }
    }
}

