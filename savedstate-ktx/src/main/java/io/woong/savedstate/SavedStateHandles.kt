package io.woong.savedstate

import androidx.lifecycle.SavedStateHandle

/**
 * Returns a value that matched to given [key],
 * or [defaultValue] if this map doesn't have the [key].
 */
public fun <T> SavedStateHandle.getOrDefault(key: String, defaultValue: T): T {
    return this.get<T>(key) ?: defaultValue
}

/**
 * Returns a value that matched to given [key],
 * or the result of [default] function parameter if this map doesn't have the [key].
 */
public inline fun <T> SavedStateHandle.getOrElse(key: String, default: () -> T): T {
    return this.get<T>(key) ?: default()
}
