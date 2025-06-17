package image.core.model

import image.core.decoder.Decoder
import image.core.fetcher.Fetcher
import image.core.util.Input

class Settings(
    val decoders: List<Decoder>,
    val fetchers: List<Fetcher<Input>>
)
