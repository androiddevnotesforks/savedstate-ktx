package io.woong.savedstate

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.StateFlow
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
public class DelegatedStateFlowTest {
    @Test
    public fun savedStateDelegate() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel

            assertThat(viewModel.stateFlow.value).isEqualTo("init")
            assertThat(
                viewModel.savedStateHandle.get<String>("stateFlow")
            ).isEqualTo("init")

            viewModel.savedStateHandle["stateFlow"] = "new"

            assertThat(viewModel.stateFlow.value).isEqualTo("new")
            assertThat(
                viewModel.savedStateHandle.get<String>("stateFlow")
            ).isEqualTo("new")
        }
    }

    public class TestActivity : ComponentActivity() {
        public val viewModel: TestViewModel by viewModels()
    }

    public class TestViewModel(public val savedStateHandle: SavedStateHandle) : ViewModel() {
        public val stateFlow: StateFlow<String> by savedStateHandle.stateFlow("init")
    }
}
