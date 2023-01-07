package io.woong.savedstate

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
public class DelegatedStateFlowTest {
    @Test
    public fun stateFlowDelegate() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel

            assertThat(viewModel.stateFlow.value).isEqualTo("init")
            assertThat(
                viewModel.savedStateHandle.get<String>("stateFlow")
            ).isEqualTo("init")

            viewModel.savedStateHandle["stateFlow"] = "first"
            assertThat(viewModel.stateFlow.value).isEqualTo("first")
            assertThat(
                viewModel.savedStateHandle.get<String>("stateFlow")
            ).isEqualTo("first")

            viewModel.savedStateHandle["stateFlow"] = "second"
            assertThat(viewModel.stateFlow.value).isEqualTo("second")
            assertThat(
                viewModel.savedStateHandle.get<String>("stateFlow")
            ).isEqualTo("second")
        }
    }

    @Test
    public fun mutableStateFlowDelegate() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel

            assertThat(viewModel.mutableStateFlow.value).isEqualTo("init")
            assertThat(
                viewModel.savedStateHandle.get<String>("mutableStateFlow")
            ).isEqualTo("init")

            viewModel.mutableStateFlow.value = "first"
            assertThat(viewModel.mutableStateFlow.value).isEqualTo("first")
            assertThat(
                viewModel.savedStateHandle.get<String>("mutableStateFlow")
            ).isEqualTo("first")

            viewModel.mutableStateFlow.value = "second"
            assertThat(viewModel.mutableStateFlow.value).isEqualTo("second")
            assertThat(
                viewModel.savedStateHandle.get<String>("mutableStateFlow")
            ).isEqualTo("second")
        }
    }

    public class TestActivity : ComponentActivity() {
        public val viewModel: TestViewModel by viewModels()
    }

    public class TestViewModel(public val savedStateHandle: SavedStateHandle) : ViewModel() {
        public val stateFlow: StateFlow<String> by savedStateHandle.stateFlow("init")

        @OptIn(ExperimentalSavedStateKtxApi::class)
        public val mutableStateFlow: MutableStateFlow<String>
            by savedStateHandle.mutableStateFlow("init", viewModelScope)
    }
}
