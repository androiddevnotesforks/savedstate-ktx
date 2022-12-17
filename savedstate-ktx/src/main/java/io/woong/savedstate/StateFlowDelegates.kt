package io.woong.savedstate

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KProperty

/**
 * Returns a [StateFlow] delegate that emits value stored in [SavedStateHandle].
 *
 * ```
 * class ExampleViewModel(savedStateHandle: SavedStateHandle) {
 *     val foo: StateFlow<Int> by savedStateHandle.stateFlow(0)
 * }
 * ```
 *
 * @param initialValue Initial value of this [StateFlow].
 */
public fun <T> SavedStateHandle.stateFlow(initialValue: T): StateFlowDelegateProvider<T> {
    return StateFlowDelegateProvider(savedStateHandle = this, initialValue)
}

public class StateFlowDelegateProvider<T>(
    private val savedStateHandle: SavedStateHandle,
    private val initialValue: T
) {
    public operator fun provideDelegate(self: Any?, property: KProperty<*>): StateFlowDelegate<T> {
        val key = property.name
        return StateFlowDelegate(savedStateHandle, key, initialValue)
    }
}

public class StateFlowDelegate<T>(
    savedStateHandle: SavedStateHandle,
    key: String,
    initialValue: T
) {
    private val stateFlow: StateFlow<T> = savedStateHandle.getStateFlow(key, initialValue)

    public operator fun getValue(self: Any?, property: KProperty<*>): StateFlow<T> {
        return stateFlow
    }
}
