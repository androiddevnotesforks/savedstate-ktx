package io.woong.savedstate

import androidx.lifecycle.SavedStateHandle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
public class SavedStateHandlesTest {
    @Suppress("RemoveExplicitTypeArguments")
    @Test
    public fun testGetterExtensions() {
        val handle = SavedStateHandle()

        handle["test"] = 100
        assertThat(handle.contains("test")).isTrue()
        assertThat(handle.getOrDefault<Int>("test", 1)).isEqualTo(100)
        assertThat(handle.getOrElse<Int>("test") { 1 + 1 }).isEqualTo(100)

        assertThat(handle.contains("nothing")).isFalse()
        assertThat(handle.getOrDefault<Int>("nothing", 1)).isEqualTo(1)
        assertThat(handle.getOrElse<Int>("nothing") { 1 + 1 }).isEqualTo(2)
    }
}
