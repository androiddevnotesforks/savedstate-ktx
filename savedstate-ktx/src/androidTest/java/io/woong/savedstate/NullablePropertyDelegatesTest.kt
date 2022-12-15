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
public class NullablePropertyDelegatorTest {
    private lateinit var context: Context

    @Before
    public fun init() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    public fun simplestDelegate() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.simpleValue).isNull()
            assertThat(viewModel.simpleVariable).isNull()

            viewModel.simpleVariable = "test"
            assertThat(viewModel.simpleVariable).isEqualTo("test")
        }
    }

    @Test
    public fun nullableWithInitialValue() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.initializedValue).isEqualTo("a")
            assertThat(viewModel.initializedVariable).isEqualTo("b")

            viewModel.initializedVariable = "c"
            assertThat(viewModel.initializedVariable).isEqualTo("c")
        }
    }

    @Test
    public fun stateSaving() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            viewModel.simpleVariable = "aaa"
            viewModel.initializedVariable = "bbb"

            assertThat(viewModel.simpleValue).isNull()
            assertThat(viewModel.simpleVariable).isEqualTo("aaa")
            assertThat(viewModel.initializedValue).isEqualTo("a")
            assertThat(viewModel.initializedVariable).isEqualTo("bbb")
        }
        scenario.recreate()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.simpleValue).isNull()
            assertThat(viewModel.simpleVariable).isEqualTo("aaa")
            assertThat(viewModel.initializedValue).isEqualTo("a")
            assertThat(viewModel.initializedVariable).isEqualTo("bbb")
        }
    }

    public class TestActivity : ComponentActivity() {
        public val viewModel: TestViewModel by viewModels()
    }

    public class TestViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
        public val simpleValue: String? by savedStateHandle
        public var simpleVariable: String? by savedStateHandle

        public val initializedValue: String? by savedStateHandle.nullable("a")
        public var initializedVariable: String? by savedStateHandle.nullable("b")
    }
}
