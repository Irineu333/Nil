package com.neoutils.nil.fetcher.network.impl

import com.neoutils.nil.core.scope.Extras
import com.neoutils.nil.core.source.Fetcher
import com.neoutils.nil.core.util.Resource
import com.neoutils.nil.fetcher.network.model.RequestNetwork
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

val HeadersExtrasKey = Extras.Key<Map<String, String>>(mapOf())

val ProgressMonitorExtrasKey = Extras.Key(default = true)

class NetworkFetcher(
    private val client: HttpClient = HttpClient()
) : Fetcher<RequestNetwork>(RequestNetwork::class) {

    override fun fetch(
        input: RequestNetwork,
        extras: Extras
    ) = callbackFlow {

        try {
            val progress = extras[ProgressMonitorExtrasKey]
            val headers = extras[HeadersExtrasKey]

            val response = client.request(input.url) {
                method = input.method

                headers.forEach {
                    this.headers[it.key] = it.value
                }

                if (progress) {
                    onProgress { progress ->
                        send(Resource.Loading(progress))
                    }
                }
            }

            send(Resource.Result.Success(value = response.bodyAsBytes()))
        } catch (error: Throwable) {
            send(Resource.Result.Failure(error))
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
