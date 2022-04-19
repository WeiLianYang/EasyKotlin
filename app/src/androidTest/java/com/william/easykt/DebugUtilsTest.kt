package com.william.easykt

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.william.easykt.utils.isDebuggable
import com.william.easykt.utils.isUnderTraced
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author William
 * @date 2022/4/12 15:30
 * Class Comment：
 */
@RunWith(AndroidJUnit4::class)
class DebugUtilsTest {

    @Test
    fun checkDebuggable() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val result = isDebuggable(appContext)
        Assert.assertTrue(result)
    }

    @Test
    fun checkUnderTraced() {
        val result = isUnderTraced()
        Assert.assertFalse(result)
    }
}