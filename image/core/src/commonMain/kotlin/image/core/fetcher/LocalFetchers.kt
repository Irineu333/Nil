package image.core.fetcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import image.core.util.Input

val LocalFetchers = compositionLocalOf<List<Fetcher<Input>>> { listOf() }

data class FetchersScope(
    val fetchers: MutableList<Fetcher<Input>> = mutableListOf()
) {
    fun build() = fetchers.toList()

    companion object {

        @Composable
        fun fromLocal(): FetchersScope {

            val fetchers = LocalFetchers.current

            return remember(fetchers) { FetchersScope(fetchers.toMutableList()) }
        }
    }
}
