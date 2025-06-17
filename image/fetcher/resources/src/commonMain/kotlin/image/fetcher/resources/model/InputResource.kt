package image.fetcher.resources.model

import image.core.util.Input
import org.jetbrains.compose.resources.DrawableResource

data class InputResource(
    val res: DrawableResource
) : Input
