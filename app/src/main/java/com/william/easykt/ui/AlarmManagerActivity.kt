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

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.logI
import com.william.base_component.extension.logV
import com.william.base_component.extension.logW
import com.william.base_component.utils.showSnackbar
import com.william.easykt.databinding.ActivityAlarmManagerBinding
import com.william.easykt.receiver.AlarmReceiver
import java.util.*

/**
 * author : WilliamYang
 * date : 2022/7/29 10:25
 * description : 闹钟使用
 *
 * 1.如果声明的triggerAtMillis是一个过去的时间，闹钟将会立即被触发。
 * 2.如果已经有一个相同intent的闹钟被设置过了，那么前一个闹钟将会取消，被新设置的闹钟所代替。
 *
 */
class AlarmManagerActivity : BaseActivity() {

    override val viewBinding: ActivityAlarmManagerBinding by bindingView()

    override fun initData() {
        setTitleText("Alarm Manager")
    }

    override fun initAction() {
        viewBinding.btnSetExactAlarm1.setOnClickListener {
            setExactAlarm1(it.context)
        }

        viewBinding.btnSetExactAlarm2.setOnClickListener {
            setExactAlarm2(it.context)
        }

        viewBinding.btnSetExactAlarm3.setOnClickListener {
            setExactAlarm3(it.context)
        }

        viewBinding.btnSetAlarm1.setOnClickListener {
            setAlarm1(it.context)
        }

        viewBinding.btnSetAlarm2.setOnClickListener {
            setAlarm2(it.context)
        }

        viewBinding.btnSetAlarm3.setOnClickListener {
        }

        viewBinding.btnCancelAlarm.setOnClickListener {
            cancelAlarm(it.context)
        }
    }

    /**
     * 为了鼓励应用节省系统资源，以 Android 12 及更高版本为目标平台且设置了精确的闹钟的应用必须能够访问“闹钟和提醒”功能，该功能显示在系统设置的特殊应用访问权限屏幕中。
     * 如需获取这种特殊应用访问权限，请在清单中请求 SCHEDULE_EXACT_ALARM 权限。
     */
    private fun setExactAlarm1(context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(
            context, 1, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val triggerAtMillis = System.currentTimeMillis() + 5 * 1000

        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        if (manager?.canScheduleExactAlarms() == true) {
            manager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender)
            "set exact alarm success 1".logV()
        } else {
            showSnackbar(viewBinding.root, "请在设置中为应用开启闹钟权限")
        }
    }

    private fun setExactAlarm2(context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(
            context, 1, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

        // 设置 精确的时间 闹钟
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 5)

        if (manager?.canScheduleExactAlarms() == true) {
            manager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, sender)
            "set exact alarm success 2".logV()
        } else {
            showSnackbar(viewBinding.root, "请在设置中为应用开启闹钟权限")
        }
    }

    private fun setExactAlarm3(context: Context) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val triggerAtMillis = System.currentTimeMillis() + 5 * 1000
        // 5秒后
        if (manager?.canScheduleExactAlarms() == true) {
            manager.setExact(
                AlarmManager.RTC_WAKEUP, triggerAtMillis, "sender", {
                    "OnAlarmListener, onAlarm()".logI()
                }, Handler(Looper.getMainLooper())
            )

            "set exact alarm success 3".logV()
        } else {
            showSnackbar(viewBinding.root, "请在设置中为应用开启闹钟权限")
        }
    }

    private fun setAlarm1(context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(
            context, 1, intent, PendingIntent.FLAG_IMMUTABLE
        )
        val triggerAtMillis = System.currentTimeMillis() + 5 * 1000

        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        manager?.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender)
        "set alarm success 1".logV()
    }

    private fun setAlarm2(context: Context) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val triggerAtMillis = System.currentTimeMillis() + 5 * 1000
        // 5秒后
        manager?.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, "sender", {
            "OnAlarmListener, onAlarm()".logI()
        }, null)

        "set alarm success 2".logV()
    }

    private fun cancelAlarm(context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(
            context, 1, intent, PendingIntent.FLAG_IMMUTABLE
        )
        // 5秒后
        val triggerAtMillis = System.currentTimeMillis() + 5 * 1000

        val manager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        if (manager?.canScheduleExactAlarms() == true) {
            manager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender)
            "set exact alarm success...".logV()
        } else {
            showSnackbar(viewBinding.root, "请在设置中为应用开启闹钟权限")
        }

        manager?.cancel(sender)

        "cancel exact alarm success...".logW()
    }

}