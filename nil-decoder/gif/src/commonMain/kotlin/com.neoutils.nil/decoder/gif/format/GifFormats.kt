package com.neoutils.nil.decoder.gif.format

import com.neoutils.nil.decoder.gif.util.signature

// https://en.wikipedia.org/wiki/List_of_file_signatures
internal val GIF87A_SPEC = signature(0x47, 0x49, 0x46, 0x38, 0x37, 0x61)
internal val GIF89A_SPEC = signature(0x47, 0x49, 0x46, 0x38, 0x39, 0x61)
