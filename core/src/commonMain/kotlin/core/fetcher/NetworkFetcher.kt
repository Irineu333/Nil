package core.fetcher

import core.extension.toRawImage
import core.model.Request
import core.util.RawImage
import core.util.Resource
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
    override suspend fun get(input: Request): Resource.Result<RawImage> {
        return runCatching {
            client.request(input.url) {
                method = input.method

                input.headers.forEach {
                    headers[it.first] = it.second
                }
            }
        }.map {
            withContext(Dispatchers.Default) {
                it.bodyAsBytes().toRawImage()
            }
        }.map { rawImage ->
            Resource.Result.Success(data = rawImage)
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
        }.map {
            withContext(Dispatchers.Default) {
                it.bodyAsBytes().toRawImage()
            }
        }.onSuccess { rawImage ->
            withContext(Dispatchers.Main) {
                send(Resource.Result.Success(data = rawImage))
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
