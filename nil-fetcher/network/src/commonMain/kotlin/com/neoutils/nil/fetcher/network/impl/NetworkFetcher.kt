package com.neoutils.nil.fetcher.network.impl

import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.fetcher.network.model.RequestNetwork
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext

val HeadersExtrasKey = Extras.Key<Map<String, String>>(mapOf())

class NetworkFetcher(
    private val client: HttpClient = HttpClient()
) : Fetcher<RequestNetwork>(RequestNetwork::class) {

    override suspend fun get(
        input: RequestNetwork,
        extras: Extras
    ) = runCatching {

        val headers = extras[HeadersExtrasKey]

        client.request(input.url) {
            method = input.method

            headers.forEach {
                this.headers[it.key] = it.value
            }
        }
    }.map { response ->
        withContext(Dispatchers.Default) {
            response.bodyAsBytes()
        }
    }.map { bytes ->
        Resource.Result.Success(value = bytes)
    }.getOrElse {
        Resource.Result.Failure(it)
    }

    override fun fetch(
        input: RequestNetwork,
        extras: Extras
    ) = channelFlow {
        runCatching {
            val headers = extras[HeadersExtrasKey]

            client.request(input.url) {
                method = input.method

                headers.forEach {
                    this.headers[it.key] = it.value
                }

                onProgress { progress ->
                    withContext(Dispatchers.Main) {
                        send(Resource.Loading(progress))
                    }
                }
            }
        }.map { response ->
            withContext(Dispatchers.Default) {
                response.bodyAsBytes()
            }
        }.onSuccess { bytes ->
            withContext(Dispatchers.Main) {
                send(Resource.Result.Success(value = bytes))
            }
        }.onFailure {
            withContext(Dispatchers.Main) {
                send(Resource.Result.Failure(it))
            }
        }

        awaitClose()
    }
}

private fun HttpRequestBuilder.onProgress(
    update: suspend (progress: Float?) -> Unit
) = onDownload { bytesSentTotal, contentLength ->
    update(
        contentLength?.let {
            bytesSentTotal.toFloat()
                .div(contentLength.toFloat())
                .coerceIn(minimumValue = 0f, maximumValue = 1f)
                .takeUnless { it.isNaN() }
        }
    )
}
