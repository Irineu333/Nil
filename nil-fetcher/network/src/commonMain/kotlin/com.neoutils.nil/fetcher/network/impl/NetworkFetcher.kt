package com.neoutils.nil.fetcher.network.impl

import com.neoutils.nil.fetcher.network.model.InputRequest
import com.neoutils.nil.core.fetcher.Fetcher
import com.neoutils.nil.core.util.Resource
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext

class NetworkFetcher(
    private val client: HttpClient = HttpClient()
) : Fetcher<InputRequest>(InputRequest::class) {

    override suspend fun get(input: InputRequest): Resource.Result<ByteArray> {
        return runCatching {
            client.request(input.url) {
                method = input.method

                input.headers.forEach {
                    headers[it.key] = it.value
                }
            }
        }.map { response ->
            withContext(Dispatchers.Default) {
                response.bodyAsBytes()
            }
        }.map { bytes ->
            Resource.Result.Success(data = bytes)
        }.getOrElse {
            Resource.Result.Failure(it)
        }
    }

    override fun fetch(input: InputRequest) = channelFlow {
        runCatching {
            client.request(input.url) {
                method = input.method

                input.headers.forEach {
                    headers[it.key] = it.value
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
                send(Resource.Result.Success(data = bytes))
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
