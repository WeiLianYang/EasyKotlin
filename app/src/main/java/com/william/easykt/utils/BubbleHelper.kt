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

package com.william.easykt.utils

import android.annotation.SuppressLint
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.LocusId
import android.content.pm.ShortcutInfo
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.core.net.toUri
import com.william.base_component.extension.notificationManager
import com.william.base_component.extension.shortcutManager
import com.william.easykt.MainActivity
import com.william.easykt.R
import com.william.easykt.ui.BubbleActivity


/**
 * @author William
 * @date 2022/4/9 22:39
 * Class Comment：气泡帮助类
 */

private const val CHANNEL_NEW_MESSAGES = "new_messages"

/**
 * 创建通知渠道
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun setUpNotificationChannels(context: Context) {
    if (context.notificationManager?.getNotificationChannel(CHANNEL_NEW_MESSAGES) == null) {
        context.notificationManager?.createNotificationChannel(
            NotificationChannel(
                CHANNEL_NEW_MESSAGES,
                context.getString(R.string.channel_new_messages),
                // The importance must be IMPORTANCE_HIGH to show Bubbles.
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.channel_new_messages_description)
            }
        )
    }
}

/**
 * 创建气泡
 */
@SuppressLint("UnspecifiedImmutableFlag")
@RequiresApi(Build.VERSION_CODES.R)
fun createBubble(context: Context) {
    setUpNotificationChannels(context)
    updateShortcuts(context)

    // Create bubble intent
    val contentUri = "https://ek.example.com/bubble/123".toUri()
    val target = Intent(context, BubbleActivity::class.java).setAction(Intent.ACTION_VIEW)
        .setData(contentUri)
    val bubbleIntent =
        PendingIntent.getActivity(context, 2, target, PendingIntent.FLAG_UPDATE_CURRENT)

    val person = Person.Builder()
        .setName("William")
        .setImportant(true)
        .build()

    // Create bubble metadata
    val bubbleData = Notification.BubbleMetadata.Builder(
        bubbleIntent,
        Icon.createWithAdaptiveBitmapContentUri("content://com.example.android.bubbles/icon/1")
    )
        .setDesiredHeight(600)
//            .setAutoExpandBubble(true)
//            .setSuppressNotification(true)
        .build()

    val builder =
        Notification.Builder(context, CHANNEL_NEW_MESSAGES)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    1,
                    Intent(context, MainActivity::class.java).putExtra("ek_type", 1),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setContentTitle("Content title!")
            .setSmallIcon(R.drawable.app_logo)
            .setShortcutId(shortcutId)
            .setBubbleMetadata(bubbleData)
            .addPerson(person)

    builder.style = Notification.MessagingStyle(person).apply {
        addMessage(
            "you have a new message", System.currentTimeMillis(), person
        )
    }.setGroupConversation(false)

    context.notificationManager?.notify(1000, builder.build())
}

private const val shortcutId = "shortcut_1"

@RequiresApi(Build.VERSION_CODES.Q)
@WorkerThread
private fun updateShortcuts(context: Context) {
    val icon = Icon.createWithAdaptiveBitmap(
        context.resources.assets.open("cat.png").use { input ->
            BitmapFactory.decodeStream(input)
        }
    )

    val shortcuts = ShortcutInfo.Builder(context, shortcutId)
        .setLocusId(LocusId(shortcutId))
        .setActivity(ComponentName(context, MainActivity::class.java))
        .setShortLabel("Label-1")
        .setIcon(icon)
        .setLongLived(true)
        .setCategories(setOf("com.example.android.bubbles.category.TEXT_SHARE_TARGET"))
        .setIntent(
            Intent(context, MainActivity::class.java)
                .setAction(Intent.ACTION_VIEW)
                .setData(
                    Uri.parse(
                        "https://ek.example.com/bubble/123"
                    )
                )
        )
        .setPerson(
            Person.Builder()
                .setName("William")
                .setIcon(icon)
                .build()
        )
        .build()

    context.shortcutManager?.addDynamicShortcuts(arrayListOf(shortcuts))
}