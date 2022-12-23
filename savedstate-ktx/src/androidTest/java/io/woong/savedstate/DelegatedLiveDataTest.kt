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

            assertThat(viewModel.liveData.value).isNull()
            assertThat(
                viewModel.savedStateHandle.get<String>("_liveData")
            ).isNull()

            viewModel.setLiveData("new")

            assertThat(viewModel.liveData.value).isEqualTo("new")
            assertThat(
                viewModel.savedStateHandle.get<String>("_liveData")
            ).isEqualTo("new")
        }
    }

    @Test
    public fun initializedLiveDataDelegate() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel

            assertThat(viewModel.initializedLiveData.value).isEqualTo("init")
            assertThat(
                viewModel.savedStateHandle.get<String>("_initializedLiveData")
            ).isEqualTo("init")

            viewModel.setInitializedLiveData("new")

            assertThat(viewModel.initializedLiveData.value).isEqualTo("new")
            assertThat(
                viewModel.savedStateHandle.get<String>("_initializedLiveData")
            ).isEqualTo("new")
        }
    }

    public class TestActivity : ComponentActivity() {
        public val viewModel: TestViewModel by viewModels()
    }

    public class TestViewModel(public val savedStateHandle: SavedStateHandle) : ViewModel() {
        private val _liveData: MutableLiveData<String> by savedStateHandle.liveData()
        public val liveData: LiveData<String>
            get() = _liveData

        private val _initializedLiveData by savedStateHandle.liveData("init")
        public val initializedLiveData: LiveData<String>
            get() = _initializedLiveData

        public fun setLiveData(value: String) {
            _liveData.value = value
        }

        public fun setInitializedLiveData(value: String) {
            _initializedLiveData.value = value
        }
    }
}
