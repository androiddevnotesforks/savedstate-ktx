package io.woong.savedstate

import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KProperty

/**
 * Delegates [SavedStateHandle.getValue] operation to property.
 * The delegated property returns a nullable value that stored in [SavedStateHandle] with a key.
 * The key of the property is the name of the property.
 *
 * ```
 * class ExampleViewModel(savedStateHandle: SavedStateHandle) {
 *     // Basic way to get value from saved state handle.
 *     val foo: String?
 *         get() = savedStateHandle["foo"]
 *
 *     // New way using this library.
 *     val bar: String? by savedStateHandle
 * }
 * ```
 */
public operator fun <T> SavedStateHandle.getValue(self: Any?, property: KProperty<*>): T? {
    return this[property.name]
}

/**
 * Delegates [SavedStateHandle.setValue] operation to property.
 * The delegated property takes a nullable value and stores in [SavedStateHandle] with a key.
 * The key of the property is the name of the property.
 *
 * ```
 * class ExampleViewModel(savedStateHandle: SavedStateHandle) {
 *     // Basic way to get/set value from saved state handle.
 *     var foo: String?
 *         get() = savedStateHandle["foo"]
 *         set(value) { savedStateHandle["foo"] = value }
 *
 *     // New way using this library.
 *     var foo: String? by savedStateHandle
 * }
 * ```
 */
public operator fun <T> SavedStateHandle.setValue(self: Any?, property: KProperty<*>, value: T?) {
    this[property.name] = value
}
