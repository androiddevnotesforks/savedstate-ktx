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
public class DelegatedPropertyTest {
    private lateinit var context: Context

    @Before
    public fun init() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    public fun nullableProperty() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.nullableProperty).isNull()
            assertThat(viewModel.initializedNullableProperty).isEqualTo("init")

            viewModel.nullableProperty = "test"
            viewModel.initializedNullableProperty = "test"
            assertThat(viewModel.nullableProperty).isEqualTo("test")
            assertThat(viewModel.initializedNullableProperty).isEqualTo("test")
        }
    }

    @Test
    public fun notNullProperty() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.notNullProperty).isEqualTo("init")

            viewModel.notNullProperty = "test"
            assertThat(viewModel.notNullProperty).isEqualTo("test")
        }
    }

    public class TestActivity : ComponentActivity() {
        public val viewModel: TestViewModel by viewModels()
    }

    public class TestViewModel(public val savedStateHandle: SavedStateHandle) : ViewModel() {
        public var nullableProperty: String? by savedStateHandle.nullable()
        public var initializedNullableProperty: String? by savedStateHandle.nullable("init")

        public var notNullProperty: String by savedStateHandle.notNull("init")
    }
}
