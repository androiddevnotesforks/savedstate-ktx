package io.woong.savedstate

import android.content.Context
import android.content.Intent
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
public class PropertyDelegatorTest {
    private lateinit var context: Context

    @Before
    public fun init() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    public fun testReadingAndWriting() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.string).isNull()
            assertThat(viewModel.doubleArray).isNull()
            assertThat(viewModel.integerList).isNull()

            viewModel.string = "test"
            viewModel.doubleArray = doubleArrayOf(0.1, 0.2, 0.3)
            viewModel.doubleArray?.set(0, -0.1)
            viewModel.integerList = listOf(1, 2, 3)
            assertThat(viewModel.string).isEqualTo("test")
            assertThat(viewModel.doubleArray)
                .usingExactEquality()
                .containsExactlyElementsIn(arrayOf(-0.1, 0.2, 0.3))
            assertThat(viewModel.integerList).containsExactlyElementsIn(listOf(1, 2, 3))
        }
    }

    @Test
    public fun testAutomaticSettingFromBundle() {
        val intent = Intent(context, TestActivity::class.java)
        intent.putExtra("string", "test")
        intent.putExtra("doubleArray", doubleArrayOf(0.9, 0.8, 0.7))
        intent.putExtra("integerList", arrayListOf(9, 8, 7))

        val scenario = launchActivity<TestActivity>(intent)
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.string).isEqualTo("test")
            assertThat(viewModel.doubleArray)
                .usingExactEquality()
                .containsExactlyElementsIn(arrayOf(0.9, 0.8, 0.7))
            assertThat(viewModel.integerList).containsExactlyElementsIn(listOf(9, 8, 7))
        }
    }

    @Test
    public fun testSavedStateAfterRecreation() {
        val scenario = launchActivity<TestActivity>()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            viewModel.string = "test"
            viewModel.doubleArray = doubleArrayOf(-0.5, 0.0, 0.5)
            viewModel.integerList = listOf(2, 4, 6, 8)
        }
        scenario.recreate()
        scenario.onActivity { activity ->
            val viewModel = activity.viewModel
            assertThat(viewModel.string).isEqualTo("test")
            assertThat(viewModel.doubleArray)
                .usingExactEquality()
                .containsExactlyElementsIn(arrayOf(-0.5, 0.0, 0.5))
            assertThat(viewModel.integerList).containsExactlyElementsIn(listOf(2, 4, 6, 8))
        }
    }

    public class TestActivity : ComponentActivity() {
        public val viewModel: TestViewModel by viewModels()
    }

    public class TestViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
        public var string: String? by savedStateHandle
        public var doubleArray: DoubleArray? by savedStateHandle
        public var integerList: List<Int>? by savedStateHandle
    }
}
