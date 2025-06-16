package image.fetcher.network.model

import io.ktor.http.HttpMethod

data class Request(
    val url: String,
    val headers: List<Pair<String, String>>,
    val method: HttpMethod
) {
    class Builder(
        var headers: List<Pair<String, String>> = listOf(),
        var method: HttpMethod = HttpMethod.Companion.Get
    ) {
        fun build(url: String) = Request(
            headers = headers,
            method = method,
            url = url
        )
    }
}