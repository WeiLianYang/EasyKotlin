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

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.web.assertion.WebViewAssertions
import androidx.test.espresso.web.sugar.Web
import androidx.test.espresso.web.webdriver.DriverAtoms
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * @author William
 * @date 2022/5/20 14:22
 * Class Comment：Test Web page
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class WebPageTest {

    @get:Rule
    val mainPageRule = ActivityTestRule(MainActivity::class.java)

//    @get:Rule
//    var webPageRule: ActivityTestRule<WebActivity> =
//        object : ActivityTestRule<WebActivity>(
//            WebActivity::class.java, false, false
//        ) {
//            override fun afterActivityLaunched() {
//                // Technically we do not need to do this - WebViewActivity has javascript turned on.
//                // Other WebViews in your app may have javascript turned off, however since the only way
//                // to automate WebViews is through javascript, it must be enabled.
//                Web.onWebView().forceJavascriptEnabled()
//            }
//        }

    @Test
    fun testWebPage() {
//        val intent = Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        webPageRule.launchActivity(intent)

        // 首先滚动到需要匹配的位置，然后点击。
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    33, ViewActions.click()
                )
            )

        Web.onWebView()
            .forceJavascriptEnabled()
            .withElement(DriverAtoms.selectActiveElement())
            .perform(DriverAtoms.webClick())
            .check(
                WebViewAssertions.webMatches(
                    DriverAtoms.getText(), Matchers.notNullValue(String::class.java)
                )
            )
    }

}