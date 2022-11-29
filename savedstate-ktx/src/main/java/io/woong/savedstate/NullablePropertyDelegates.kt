package io.woong.savedstate

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KProperty

/**
 * Returns a property delegate for reading a value from [SavedStateHandle].
 *
 * Reading the property equals to read value from [SavedStateHandle].
 * The key to read value from handle is its property name.
 *
 * To define property delegate, use `by` keyword of Kotlin.
 * The property must nullable type.
 *
 * ```
 * class ExampleViewModel(savedStateHandle: SavedStateHandle) {
 *     // This code equals to below code:
 *     // val foo: String?
 *     //     get() = savedStateHandle["foo"]
 *     val foo: String? by savedStateHandle
 * }
 * ```
 */
public operator fun <T> SavedStateHandle.getValue(self: Any?, property: KProperty<*>): T? {
    return this[property.name]
}

/**
 * Returns a property delegate for writing a value to [SavedStateHandle].
 *
 * Writing the property equals to write value to [SavedStateHandle].
 * The key to write value to handle is its property name.
 *
 * To define property delegate, use `by` keyword of Kotlin.
 * The property must nullable type.
 *
 * ```
 * class ExampleViewModel(savedStateHandle: SavedStateHandle) {
 *     // This code equals to below code:
 *     // var foo: String?
 *     //     get() = savedStateHandle["foo"]
 *     //     set(value) { savedStateHandle["foo"] = value }
 *     var foo: String? by savedStateHandle
 * }
 * ```
 *
 * The type of property must nullable because [SavedStateHandle] can store `null`.
 */
public operator fun <T> SavedStateHandle.setValue(self: Any?, property: KProperty<*>, value: T?) {
    this[property.name] = value
}

/**
 * Returns a property delegate for reading and writing a nullable value into [SavedStateHandle]
 * with initial value.
 *
 * Reading the property equals to read value from [SavedStateHandle]
 * and writing equals to write value to [SavedStateHandle].
 *
 * To define initialized property delegate, use `by` keyword of Kotlin.
 * The property must nullable type.
 *
 * ```
 * class ExampleViewModel(savedStateHandle: SavedStateHandle) {
 *     // This code equals to below code:
 *     // var foo: String?
 *     //     get() = savedStateHandle["foo"]
 *     //     set(value) { savedStateHandle["foo"] = value }
 *     //
 *     // init {
 *     //     if (!savedStateHandle.contains("foo")) {
 *     //         savedStateHandle["foo"] = "init"
 *     //     }
 *     // }
 *     var foo: String? by savedStateHandle.nullable("init")
 * }
 * ```
 *
 * @param initialValue The initial value of this property.
 */
public fun <T> SavedStateHandle.nullable(initialValue: T?): NullablePropertyDelegateProvider<T> {
    return NullablePropertyDelegateProvider(savedStateHandle = this, initialValue)
}

public class NullablePropertyDelegateProvider<T>(
    private val savedStateHandle: SavedStateHandle,
    private val initialValue: T?
) {
    public operator fun provideDelegate(self: Any?, property: KProperty<*>): NullablePropertyDelegate<T> {
        val key = property.name
        if (!savedStateHandle.contains(key)) {
            savedStateHandle[key] = initialValue
        }
        return NullablePropertyDelegate(savedStateHandle, key)
    }
}

public class NullablePropertyDelegate<T>(
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
