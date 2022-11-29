package io.woong.savedstate

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KProperty

/**
 * Returns a property delegate for reading and writing a not-null value into [SavedStateHandle]
 * with initial value.
 *
 * Reading the property equals to read value from [SavedStateHandle]
 * and writing equals to write value to [SavedStateHandle].
 *
 * To define not-null property delegate, use `by` keyword of Kotlin:
 * The property must not-null type.
 *
 * ```
 * class ExampleViewModel(savedStateHandle: SavedStateHandle) {
 *     // This code equals to below code:
 *     // var foo: String
 *     //     get() = savedStateHandle["foo"]!!
 *     //     set(value) { savedStateHandle["foo"] = value }
 *     //
 *     // init {
 *     //     if (!savedStateHandle.contains("foo")) {
 *     //         savedStateHandle["foo"] = "init"
 *     //     }
 *     // }
 *     var foo: String by savedStateHandle.notNull("init")
 * }
 * ```
 *
 * @param initialValue The initial value of this property.
 */
public fun <T> SavedStateHandle.notNull(initialValue: T): NotNullPropertyDelegateProvider<T> {
    return NotNullPropertyDelegateProvider(savedStateHandle = this, initialValue)
}

public class NotNullPropertyDelegateProvider<T>(
    private val savedStateHandle: SavedStateHandle,
    private val initialValue: T
) {
    public operator fun provideDelegate(self: Any?, property: KProperty<*>): NotNullPropertyDelegate<T> {
        val key = property.name
        if (!savedStateHandle.contains(key)) {
            savedStateHandle[key] = initialValue
        }
        return NotNullPropertyDelegate(savedStateHandle, key)
    }
}

public class NotNullPropertyDelegate<T>(
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
