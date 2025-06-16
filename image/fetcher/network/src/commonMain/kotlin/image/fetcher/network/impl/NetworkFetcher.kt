package image.fetcher.network.impl

import image.core.fetcher.Fetcher
import image.core.util.Resource
import image.fetcher.network.model.Request
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
) : Fetcher<Request> {
    override suspend fun get(input: Request): Resource.Result<ByteArray> {
        return runCatching {
            client.request(input.url) {
                method = input.method

                input.headers.forEach {
                    headers[it.first] = it.second
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

    override fun fetch(input: Request) = channelFlow {
        runCatching {
            client.request(input.url) {
                method = input.method

                input.headers.forEach {
                    headers[it.first] = it.second
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
