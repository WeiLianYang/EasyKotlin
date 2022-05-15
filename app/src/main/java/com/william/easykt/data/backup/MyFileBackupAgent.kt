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
import android.app.backup.BackupDataInput
import android.app.backup.BackupDataOutput
import android.app.backup.FileBackupHelper
import android.os.ParcelFileDescriptor
import java.io.IOException


/**
 * @author William
 * @date 2022/5/14 19:42
 * Class Comment：文件备份
 */

// 文件名
const val TOP_SCORES = "scores"
const val PLAYER_STATS = "stats"

// 唯一标识备份数据集的密钥
const val FILES_BACKUP_KEY = "myfiles"

class MyFileBackupAgent : BackupAgentHelper() {

    override fun onCreate() {
        // 分配一个助手并将其添加到备份代理
        // 实例化 FileBackupHelper 时，必须包含保存到应用内部存储空间（通过 getFilesDir() 指定，与 openFileOutput() 写入文件的位置相同）的一个或多个文件的名称。
        FileBackupHelper(this, TOP_SCORES, PLAYER_STATS).also {
            addHelper(FILES_BACKUP_KEY, it)
        }
    }

    /**
     * 从设备读取应用数据，并将要备份的数据传递到备份管理器
     *
     * @param oldState 一个打开的只读 ParcelFileDescriptor，指向应用程序提供的最后一个备份状态。可能是 <code>null<code>，在这种情况下不提供先前状态，应用程序应执行完整备份。
     * @param data 一个 BackupDataOutput 对象，用于将备份数据传递给备份管理器。
     * @param newState 一个打开的、可读写的 ParcelFileDescriptor 指向一个空文件。将请求的数据写入 <code>data<code> 输出流后，应用程序应在此处记录最终备份状态。
     */
    @Throws(IOException::class)
    override fun onBackup(
        oldState: ParcelFileDescriptor,
        data: BackupDataOutput,
        newState: ParcelFileDescriptor
    ) {
        // 在 FileBackupHelper 执行备份时保持锁定
        // 内部存储空间读取和写入文件不具备线程安全性。为了确保您的备份代理不会与 activity 同时读取或写入文件，您必须在每次执行读取或写入操作时都使用同步语句
        synchronized(dataLock) {
            super.onBackup(oldState, data, newState)
        }
    }

    /**
     * 该方法会传递您的备份数据，而通过备份数据，您的应用可以恢复之前的状态
     *
     *
     */
    @Throws(IOException::class)
    override fun onRestore(
        data: BackupDataInput,
        appVersionCode: Int,
        newState: ParcelFileDescriptor
    ) {
        // 在 FileBackupHelper 恢复文件时保持锁定。
        // 内部存储空间读取和写入文件不具备线程安全性。为了确保您的备份代理不会与 activity 同时读取或写入文件，您必须在每次执行读取或写入操作时都使用同步语句
        synchronized(dataLock) {
            super.onRestore(data, appVersionCode, newState)
        }
    }

    companion object {
        val dataLock = Any()
    }
}