package io.woong.savedstate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import kotlin.reflect.KProperty

/**
 * Returns a [MutableLiveData] that access data associated with the [SavedStateHandle].
 * The value of [LiveData][androidx.lifecycle.LiveData] is automatically saved in [SavedStateHandle].
 *
 * If the [initialValue] is `null`, the [LiveData][androidx.lifecycle.LiveData] will have `null` value.
 * To avoid `null`, you should set [initialValue].
 *
 * ```
 * class ExampleViewModel(savedStateHandle: SavedStateHandle) {
 *     private val _foo: MutableLiveData<Int> by savedStateHandle.liveData()
 *     val foo: LiveData<Int>
 *         get() = _foo
 *
 *     private val _bar: MutableLiveData<Int> by savedStateHandle.liveData(0)
 *     val bar: LiveData<Int>
 *         get() = _bar
 * }
 * ```
 *
 * @param initialValue Optional initial value of this [LiveData][androidx.lifecycle.LiveData].
 */
public fun <T> SavedStateHandle.liveData(initialValue: T? = null): LiveDataDelegateProvider<T> {
    return LiveDataDelegateProvider(savedStateHandle = this, initialValue)
}

public class LiveDataDelegateProvider<T>(
    private val savedStateHandle: SavedStateHandle,
    private val initialValue: T?
) {
    public operator fun provideDelegate(self: Any?, property: KProperty<*>): LiveDataDelegate<T> {
        val key = property.name
        return LiveDataDelegate(savedStateHandle, key, initialValue)
    }
}

public class LiveDataDelegate<T>(
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
