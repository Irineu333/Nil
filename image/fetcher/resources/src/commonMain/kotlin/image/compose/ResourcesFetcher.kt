package image.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import image.fetcher.ResourcesFetcher
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.rememberResourceEnvironment

@Composable
fun rememberResourceFetcher(
    environment: ResourceEnvironment = rememberResourceEnvironment()
) = remember(environment) { ResourcesFetcher(environment) }