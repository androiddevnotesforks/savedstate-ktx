package io.woong.savedstate

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KProperty

/**
 * Returns a nullable delegated property that handle value stored in the [SavedStateHandle].
 *
 * ```
 * var property: String? by savedStateHandle.nullable("init")
 * ```
 *
 * @param initialValue Optional initial value of this property.
 */
public fun <T> SavedStateHandle.nullable(initialValue: T? = null): NullableDelegatedPropertyProvider<T> {
    return NullableDelegatedPropertyProvider(savedStateHandle = this, initialValue)
}

/**
 * Returns a null safe delegated property that handle value stored in the [SavedStateHandle].
 *
 * ```
 * var property: String by savedStateHandle.notNull("init")
 * ```
 *
 * @param initialValue The initial value of this property.
 */
public fun <T : Any> SavedStateHandle.notNull(initialValue: T): NotNullDelegatedPropertyProvider<T> {
    return NotNullDelegatedPropertyProvider(savedStateHandle = this, initialValue)
}

/**
 * Internal nullable delegated property provider.
 */
public class NullableDelegatedPropertyProvider<T>(
    private val savedStateHandle: SavedStateHandle,
    private val initialValue: T?
) {
    public operator fun provideDelegate(self: Any?, property: KProperty<*>): NullableDelegatedProperty<T> {
        val key = property.name
        if (!savedStateHandle.contains(key)) {
            savedStateHandle[key] = initialValue
        }
        return NullableDelegatedProperty(savedStateHandle, key)
    }
}

/**
 * Internal implementation of nullable delegated property.
 */
public class NullableDelegatedProperty<T>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String
) {
    public operator fun getValue(self: Any?, property: KProperty<*>): T? {
        return savedStateHandle[key]
    }

    public operator fun setValue(self: Any?, property: KProperty<*>, value: T?) {
        savedStateHandle[key] = value
    }
}

/**
 * Internal null safe delegated property provider.
 */
public class NotNullDelegatedPropertyProvider<T : Any>(
    private val savedStateHandle: SavedStateHandle,
    private val initialValue: T
) {
    public operator fun provideDelegate(self: Any?, property: KProperty<*>): NotNullDelegatedProperty<T> {
        val key = property.name
        if (!savedStateHandle.contains(key)) {
            savedStateHandle[key] = initialValue
        }
        return NotNullDelegatedProperty(savedStateHandle, key)
    }
}

/**
 * Internal implementation of null safe delegated property.
 */
public class NotNullDelegatedProperty<T : Any>(
    private val savedStateHandle: SavedStateHandle,
    private val key: String
) {
    public operator fun getValue(self: Any?, property: KProperty<*>): T {
        return savedStateHandle[key]!!
    }

    public operator fun setValue(self: Any?, property: KProperty<*>, value: T) {
        savedStateHandle[key] = value
    }
}
