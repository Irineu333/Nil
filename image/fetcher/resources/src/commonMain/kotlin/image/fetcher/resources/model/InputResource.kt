package image.fetcher.resources.model

import image.core.util.Input
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ResourceEnvironment

data class InputResource(
    val res: DrawableResource,
    val environment: ResourceEnvironment
) : Input
