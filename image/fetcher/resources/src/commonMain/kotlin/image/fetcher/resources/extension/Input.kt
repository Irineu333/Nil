package image.fetcher.resources.extension

import androidx.compose.runtime.Composable
import image.core.util.Input
import image.fetcher.resources.model.InputResource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.rememberResourceEnvironment

@Composable
fun Input.Companion.resource(
    res: DrawableResource,
    environment: ResourceEnvironment = rememberResourceEnvironment()
) = InputResource(res, environment)
