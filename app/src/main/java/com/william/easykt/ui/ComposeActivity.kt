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

package com.william.easykt.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.william.easykt.R
import com.william.easykt.data.Message
import com.william.easykt.data.SampleData
import com.william.easykt.ui.theme.EasyKotlinTheme

/**
 * https://developer.android.com/jetpack/compose/tutorial
 * 声明式界面的关键思想：
 * 1. 开发者负责完全描述给定状态的界面外观
 * 2. 框架负责在状态更改时更新界面
 *
 * 特点：
 * 1. Compose 将状态转换为界面，如果状态发生变更，界面也将重新生成
 * 2. Compose 会跳过状态未变更的操作，从而效率更高
 * 3. Compose 将界面分解为可组合函数，函数不返回内容，而是生成界面
 * 4. Compose 将数据作为函数参数并生成界面，只执行输入发生更改的，会跳过未更改的部分
 * 5. Compose 尤其适用于采用单向数据流的应用架构
 * 6. Compose 布局模型不允许多遍测量，从而使嵌套的布局更高效
 * 7. Composable 是不可变的，开发者无法保留对它的引用，并在之后查询或更新其内容
 * 8. 希望在重新执行 Composable 函数时保留变量值，可以使用 remember 函数记住先前执行中用到的值，这样重用该值，避免重新分配或保持某个状态
 * 9. 重组是指在输入更改时再次调用可组合函数的过程。当函数的输入更改时，会发生这种情况。当 Compose 根据新输入重组时，它仅调用可能已更改的函数或 lambda，而跳过其余函数或 lambda。通过跳过所有未更改参数的函数或 lambda，Compose 可以高效地重组。
 */
class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EasyKotlinTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    // demo
                    // Greeting("William")

                    // single message card
                    /*
                    MessageCard(
                        Message(
                            "Android",
                            "Hey, take a look at Jetpack Compose, it's great!"
                        )
                    )
                    */

                    // conversation list
                    Conversation(SampleData.conversationSample)
                }
            }
        }
    }
}

@Composable
fun Conversation(list: List<Message>) {
    LazyColumn {
        items(list) { message ->
            MessageCard(message)
        }
    }
}

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = "App logo",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))
        // We keep track if the message is expanded or not in this
        // variable
        // 可以使用 remember 将本地状态存储在内存中，并跟踪传递给 mutableStateOf 的值的变化
        // 该值更新时，系统会自动重新绘制使用此状态的可组合项（及其子项）。我们将这一功能称为重组。
        // import androidx.compose.runtime.getValue
        // import androidx.compose.runtime.setValue
        var isExpanded by remember { mutableStateOf(false) }

        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor: Color by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier.animateContentSize().padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }

}

@Preview
@Composable
fun PreViewConversation() {
    EasyKotlinTheme {
        Conversation(SampleData.conversationSample)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun PreviewMessageCard() {
    MessageCard(
        msg = Message("William", "Hey, take a look at Jetpack Compose, it's great!")
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EasyKotlinTheme {
        Greeting("Android")
    }
}