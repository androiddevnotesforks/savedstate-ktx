package io.woong.savedstate

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty

/**
 * Returns a delegated [StateFlow] that hande value stored in the [SavedStateHandle].
 *
 * @param initialValue Initial value of this [StateFlow].
 */
public fun <T> SavedStateHandle.stateFlow(initialValue: T): StateFlowDelegateProvider<T> {
    return StateFlowDelegateProvider(savedStateHandle = this, initialValue)
}

/**
 * Internal delegated [StateFlow] provider.
 */
public class StateFlowDelegateProvider<T>(
    private val savedStateHandle: SavedStateHandle,
    private val initialValue: T
) {
    public operator fun provideDelegate(
        self: Any?,
        property: KProperty<*>
    ): DelegatedStateFlow<T> {
        val key = property.name
        return DelegatedStateFlow(savedStateHandle, key, initialValue)
    }
}

/**
 * Internal implementation of delegated [StateFlow].
 */
public class DelegatedStateFlow<T>(
    savedStateHandle: SavedStateHandle,
    key: String,
    initialValue: T
) {
    private val stateFlow: StateFlow<T> = savedStateHandle.getStateFlow(key, initialValue)

    public operator fun getValue(self: Any?, property: KProperty<*>): StateFlow<T> {
        return stateFlow
    }
}
