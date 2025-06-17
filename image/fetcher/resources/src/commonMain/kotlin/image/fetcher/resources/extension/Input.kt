package image.fetcher.resources.extension

import image.core.util.Input
import image.fetcher.resources.model.InputResource
import org.jetbrains.compose.resources.DrawableResource

fun Input.Companion.resource(res: DrawableResource) = InputResource(res)
