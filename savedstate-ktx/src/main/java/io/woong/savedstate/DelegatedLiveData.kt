package io.woong.savedstate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KProperty

/**
 * Returns a delegated [LiveData][androidx.lifecycle.LiveData] that handle value
 * stored in the [SavedStateHandle].
 *
 * ```
 * val liveData: MutableLiveData<String> by savedStateHandle.liveData()
 * ```
 *
 * @param initialValue Optional initial value of this [LiveData][androidx.lifecycle.LiveData].
 */
public fun <T> SavedStateHandle.liveData(initialValue: T? = null): LiveDataDelegateProvider<T> {
    return LiveDataDelegateProvider(savedStateHandle = this, initialValue)
}

/**
 * Internal delegated [LiveData][androidx.lifecycle.LiveData] provider.
 */
public class LiveDataDelegateProvider<T>(
    private val savedStateHandle: SavedStateHandle,
    private val initialValue: T?
) {
    public operator fun provideDelegate(
        self: Any?,
        property: KProperty<*>
    ): DelegatedLiveData<T> {
        val key = property.name
        return DelegatedLiveData(savedStateHandle, key, initialValue)
    }
}

/**
 * Internal implementation of delegated [LiveData][androidx.lifecycle.LiveData].
 */
public class DelegatedLiveData<T>(
    savedStateHandle: SavedStateHandle,
    key: String,
    initialValue: T?
) {
    private val liveData: MutableLiveData<T>

    init {
        liveData = if (initialValue == null) {
            savedStateHandle.getLiveData(key)
        } else {
            savedStateHandle.getLiveData(key, initialValue)
        }
    }

    public operator fun getValue(self: Any?, property: KProperty<*>): MutableLiveData<T> {
        return liveData
    }
}
