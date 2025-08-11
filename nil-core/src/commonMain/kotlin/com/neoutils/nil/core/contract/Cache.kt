package com.neoutils.nil.core.contract

interface Cache<K, T> {
    fun has(key: K): Boolean
    operator fun set(key: K, value: T)
    operator fun get(key: K): T
}
