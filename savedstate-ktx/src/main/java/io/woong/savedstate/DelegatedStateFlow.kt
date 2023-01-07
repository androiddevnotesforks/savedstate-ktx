package io.woong.savedstate

import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
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
 * Returns a delegated [MutableStateFlow] that hande value stored in the [SavedStateHandle].
 *
 * @param initialValue Initial value of this [MutableStateFlow].
 * @param coroutineScope A [CoroutineScope] to control [MutableStateFlow].
 */
@ExperimentalSavedStateKtxApi
public fun <T> SavedStateHandle.mutableStateFlow(
    initialValue: T,
    coroutineScope: CoroutineScope
): MutableStateFlowDelegateProvider<T> {
    return MutableStateFlowDelegateProvider(savedStateHandle = this, initialValue, coroutineScope)
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

/**
 * Internal delegated [MutableStateFlow] provider.
 */
@ExperimentalSavedStateKtxApi
public class MutableStateFlowDelegateProvider<T>(
    private val savedStateHandle: SavedStateHandle,
    private val initialValue: T,
    private val coroutineScope: CoroutineScope
) {
    public operator fun provideDelegate(
        self: Any?,
        property: KProperty<*>
    ): DelegatedMutableStateFlow<T> {
        val key = property.name
        return DelegatedMutableStateFlow(savedStateHandle, key, initialValue, coroutineScope)
    }
}

/**
 * Internal implementation of delegated [MutableStateFlow].
 */
@ExperimentalSavedStateKtxApi
public class DelegatedMutableStateFlow<T>(
    savedStateHandle: SavedStateHandle,
    key: String,
    initialValue: T,
    coroutineScope: CoroutineScope
) {
    private val mutableStateFlow: MutableStateFlow<T>

    init {
        val liveData = savedStateHandle.getLiveData(key, initialValue)
        val stateFlow = MutableStateFlow(initialValue)
        val observer = Observer<T?> { value ->
            if (value != stateFlow.value) {
                stateFlow.value = value
            }
        }
        liveData.observeForever(observer)
        coroutineScope.launch {
            stateFlow.onCompletion {
                liveData.removeObserver(observer)
            }.collect { value ->
                if (value != liveData.value) {
                    liveData.value = value
                }
            }
        }
        mutableStateFlow = stateFlow
    }

    public operator fun getValue(self: Any?, property: KProperty<*>): MutableStateFlow<T> {
        return mutableStateFlow
    }
}
