package io.woong.savedstate

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
public class DelegatedLiveDataTest {
    @Test
    public fun liveDataDelegate() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.intLiveData.value).isNull()

            viewModel.setInt(100)
            assertThat(viewModel.intLiveData.value).isEqualTo(100)
        }
    }

    @Test
    public fun initializedLiveDataDelegate() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.initializedIntLiveData.value).isNotNull()
            assertThat(viewModel.initializedIntLiveData.value).isEqualTo(100)

            viewModel.setInitializedInt(200)
            assertThat(viewModel.initializedIntLiveData.value).isEqualTo(200)
        }
    }

    @Test
    public fun observerWorks() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.intLiveData.value).isNull()

            var observedValue: Int? = null
            viewModel.intLiveData.observe(activity) {
                observedValue = it
            }
            viewModel.setInt(100)
            assertThat(viewModel.intLiveData.value).isEqualTo(100)
            assertThat(observedValue).isNotNull()
            assertThat(observedValue).isEqualTo(100)
        }
    }

    public class TestActivity : ComponentActivity() {
        public val viewModel: TestViewModel by viewModels()
    }

    public class TestViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
        private val _intLiveData: MutableLiveData<Int> by savedStateHandle.liveData()
        public val intLiveData: LiveData<Int>
            get() = _intLiveData

        private val _initializedIntLiveData by savedStateHandle.liveData(100)
        public val initializedIntLiveData: LiveData<Int>
            get() = _initializedIntLiveData

        public fun setInt(value: Int) {
            _intLiveData.value = value
        }

        public fun setInitializedInt(value: Int) {
            _initializedIntLiveData.value = value
        }
    }
}
