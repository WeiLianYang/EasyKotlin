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

package com.william.easykt.data.backup

import android.app.backup.BackupAgentHelper
import android.app.backup.SharedPreferencesBackupHelper


/**
 * @author William
 * @date 2022/5/14 19:38
 * Class Comment：备份 SharedPreferences
 *
 * https://developer.android.com/guide/topics/data/keyvaluebackup#SharedPreferences
 *
 * SharedPreferences 的方法具备线程安全性，因此您可以放心地从备份代理和其他 activity 中读写共享偏好设置文件。
 */


// SharedPreferences 文件的名称
const val PREFS = "user_preferences"

// 唯一标识备份数据集的密钥
const val PREFS_BACKUP_KEY = "prefs"

class MyPrefsBackupAgent : BackupAgentHelper() {

    override fun onCreate() {
        // 分配一个助手并将其添加到备份代理
        SharedPreferencesBackupHelper(this, PREFS).also {
            addHelper(PREFS_BACKUP_KEY, it)
        }
    }
}
