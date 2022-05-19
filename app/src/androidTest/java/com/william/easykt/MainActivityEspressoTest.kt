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
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.william.base_component.adapter.BaseRvViewHolder
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * @author William
 * @date 2022/5/19 10:01
 * Class Comment：MainActivity UI 测试
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityEspressoTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

//    @Rule
//    var activityScenarioRule = ActivityScenarioRule(
//        MainActivity::class.java
//    )

    @Test
    fun matchText() {
        val text =
            ApplicationProvider.getApplicationContext<Context>().resources.getString(R.string.test_card_effects)
        onView(withText(text)).check(matches(isDisplayed()))
    }

    @Test(expected = PerformException::class)
    fun itemWithText_doesNotExist() {
        // 尝试滚动到包含特殊文本的项目
        onView(ViewMatchers.withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(
                    ViewMatchers.hasDescendant(withText("item not in the list"))
                )
            )
    }

    @Test
    fun scrollToItem_checkItsText() {
        // 首先滚动到需要匹配的位置，然后点击。
        onView(ViewMatchers.withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    29, ViewActions.click()
                )
            )

        // 匹配项目中的文本并检查它是否显示。
        val itemElementText =
            ApplicationProvider.getApplicationContext<Context>().resources.getString(
                R.string.test_motion_layout
            )
        onView(withText(itemElementText)).check(matches(isDisplayed()))
    }

    @Test
    fun itemInList_hasSpecialText() {
        // 首先，使用匹配器滚动到视图持有者。
        onView(ViewMatchers.withId(R.id.recyclerView))
            .perform(RecyclerViewActions.scrollToHolder(isRoundCornerLayoutHolder()))

        // 检查项目是否有特殊文本。
        val elementText =
            ApplicationProvider.getApplicationContext<Context>().resources.getString(R.string.test_round_rect)
        onView(withText(elementText)).check(matches(isDisplayed()))
    }

    /**
     * 匹配列表中的 Round Corner Layout 条目
     */
    private fun isRoundCornerLayoutHolder(): Matcher<BaseRvViewHolder?> {
        val text =
            ApplicationProvider.getApplicationContext<Context>().resources.getString(R.string.test_round_rect)
        return object : TypeSafeMatcher<BaseRvViewHolder?>() {

            override fun describeTo(description: Description) {
                description.appendText(text)
            }

            override fun matchesSafely(holder: BaseRvViewHolder?): Boolean {
                return holder?.getView<TextView>(R.id.text)?.text == text
            }
        }
    }
}
