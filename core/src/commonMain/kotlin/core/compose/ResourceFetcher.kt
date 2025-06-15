package core.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import core.fetcher.ResourceFetcher
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.rememberResourceEnvironment

@Composable
fun rememberResourceFetcher(
    environment: ResourceEnvironment = rememberResourceEnvironment()
) = remember(environment) { ResourceFetcher(environment) }