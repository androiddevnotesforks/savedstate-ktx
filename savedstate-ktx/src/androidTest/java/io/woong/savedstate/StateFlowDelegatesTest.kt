package io.woong.savedstate

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
public class StateFlowDelegatesTest {
    @Test
    public fun getValue() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.intStateFlow.value).isEqualTo(1)

            viewModel.setInt(2)
            assertThat(viewModel.intStateFlow.value).isEqualTo(2)
        }
    }

    public class TestActivity : ComponentActivity() {
        public val viewModel: TestViewModel by viewModels()
    }

    public class TestViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
        public val intStateFlow: StateFlow<Int> by savedStateHandle.stateFlow(1)

        public fun setInt(value: Int) {
            savedStateHandle["intStateFlow"] = value
        }
    }
}
