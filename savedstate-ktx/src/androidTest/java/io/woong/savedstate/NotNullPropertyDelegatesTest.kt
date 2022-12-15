package io.woong.savedstate

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
public class NotNullPropertyDelegatorTest {
    private lateinit var context: Context

    @Before
    public fun init() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    public fun notNullWithInitialValue() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.notNullValue).isEqualTo("a")
            assertThat(viewModel.notNullVariable).isEqualTo("b")

            viewModel.notNullVariable = "c"
            assertThat(viewModel.notNullVariable).isEqualTo("c")
        }
    }

    @Test
    public fun stateSaving() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            viewModel.notNullVariable = "bbb"

            assertThat(viewModel.notNullValue).isEqualTo("a")
            assertThat(viewModel.notNullVariable).isEqualTo("bbb")
        }
        scenario.recreate()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.notNullValue).isEqualTo("a")
            assertThat(viewModel.notNullVariable).isEqualTo("bbb")
        }
    }

    public class TestActivity : ComponentActivity() {
        public val viewModel: TestViewModel by viewModels()
    }

    public class TestViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
        public val notNullValue: String by savedStateHandle.notNull("a")
        public var notNullVariable: String by savedStateHandle.notNull("b")
    }
}
