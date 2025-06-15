package core.codec

interface Decoder<T, R> {
    fun decode(input: T): R
}