/*
 * Copyright WeiLianYang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.william.easykt

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import com.william.base_component.extension.logD
import com.william.base_component.extension.logI
import com.william.easykt.utils.AesManager
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.startsWith
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * @author William
 * @date 2022/4/3 21:52
 * Class Comment：AES Page Espresso test @see[com.william.easykt.ui.AesEncryptActivity]
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class AesPageTest {

    @get:Rule
    var mainRule = ActivityTestRule(MainActivity::class.java)

    var encryptResult: String? = null

    @Before
    fun preTest() {
        encryptResult = AesManager.encrypt(plainText, associatedData)
        "encrypt result: $encryptResult".logD()

        val decryptResult = AesManager.decrypt(encryptResult!!, associatedData)
        "decrypt result: $decryptResult".logI()
    }

    @Test
    fun testEncrypt() {
        // 滑动到第25个位置，然后点击
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    25, click()
                )
            )

        // 输入明文和关联数据
        onView(withId(R.id.et_plain)).perform(typeText(plainText), closeSoftKeyboard())
        onView(withId(R.id.et_associated)).perform(typeText(associatedData), closeSoftKeyboard())

        // 执行加密
        onView(withId(R.id.btn_encrypt)).perform(click())

        // 校验加密过程是否发生了异常
        val errorText =
            ApplicationProvider.getApplicationContext<Context>().resources.getString(R.string.error_cannot_encrypt)
        // 检查是否有 错误信息开头的文本
        val textMatcher = withText(startsWith(errorText))
        onView(withId(R.id.et_cipher)).check(matches(not(textMatcher)))
    }


    @Test
    fun testDecrypt() {
        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    25, click()
                )
            )

        // 输入加密结果和关联数据
        onView(withId(R.id.et_cipher)).perform(typeText(encryptResult), closeSoftKeyboard())
        onView(withId(R.id.et_associated)).perform(typeText(associatedData), closeSoftKeyboard())

        // 执行解密
        onView(withId(R.id.btn_decrypt)).perform(click())

        // 校验解密结果是否相符
        onView(withId(R.id.et_plain)).check(matches(withText(plainText)))
    }

    companion object {

        const val plainText = "123!@#\$%^&*()/ABCdef"
        const val associatedData = "abc123"
    }
}