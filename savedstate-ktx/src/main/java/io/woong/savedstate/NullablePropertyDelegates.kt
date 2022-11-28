package io.woong.savedstate

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KProperty

/**
 * Returns a property delegate for reading a value from [SavedStateHandle].
 *
 * Reading the property equals to read value from [SavedStateHandle].
 * The key to read value from handle is its property name.
 *
 * To define property delegate, use `by` keyword of Kotlin:
 *
 * ```
 * class ExampleViewModel(savedStateHandle: SavedStateHandle) {
 *     // This code equals to below code:
 *     // val foo: String?
 *     //     get() = savedStateHandle["foo"]
 *     val foo: String? by savedStateHandle
 * }
 * ```
 *
 * The type of property must nullable because [SavedStateHandle] can returns `null`.
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
