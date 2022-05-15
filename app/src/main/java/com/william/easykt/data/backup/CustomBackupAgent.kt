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

import android.app.backup.BackupAgent
import android.app.backup.BackupDataInput
import android.app.backup.BackupDataOutput
import android.os.ParcelFileDescriptor
import com.william.base_component.extension.logD
import java.io.*


/**
 * @author William
 * @date 2022/5/14 19:53
 * Class Comment：扩展 BackupAgent
 *
 * https://developer.android.com/guide/topics/data/keyvaluebackup
 *
 */
class CustomBackupAgent : BackupAgent() {

    private var name: String? = null
    private var age: Int = 0
    private val dataFile by lazy {
        File("test path")
    }

    companion object {
        const val KEY_NAME = "key_name"
    }

    /**
     * 从设备读取应用数据，并将要备份的数据传递到备份管理器
     *
     * @param oldState 一个打开的只读 ParcelFileDescriptor，指向应用程序提供的最后一个备份状态。可能是 <code>null<code>，在这种情况下不提供先前状态，应用程序应执行完整备份。
     * @param data 一个 BackupDataOutput 对象，用于将备份数据传递给备份管理器。
     * @param newState 一个打开的、可读写的 ParcelFileDescriptor 指向一个空文件。将请求的数据写入 <code>data<code> 输出流后，应用程序应在此处记录最终备份状态。
     */
    override fun onBackup(
        oldState: ParcelFileDescriptor?,
        data: BackupDataOutput?,
        newState: ParcelFileDescriptor?
    ) {
        // 通过将 oldState 与当前数据进行比较，检查自上次备份以来您的数据是否发生过变化。
        // 您读取 oldState 中的数据的方式取决于您最初将其写入 newState 的方式（详见第 3 步）。
        // 如需记录文件的状态，最简单的方法就是使用它的上次修改时间戳。
        // 以下代码段展示了如何从 oldState 中读取时间戳并进行比较：
        val fileInputStream = FileInputStream(oldState?.fileDescriptor)
        val dataInputStream = DataInputStream(fileInputStream)
        try {
            // Get the last modified timestamp from the state file and data file
            val stateModified = dataInputStream.readLong()
            val fileModified: Long = dataFile.lastModified()
            if (stateModified != fileModified) {
                // 文件已修改，请备份或设备上的时间已更改，请注意安全并进行备份
                "The file has been modified, so do a backup".logD()

                val buffer: ByteArray = ByteArrayOutputStream().run {
                    DataOutputStream(this).apply {
                        writeChars(name)
                        writeInt(age)
                    }
                    toByteArray()
                }
                val len: Int = buffer.size
                data?.apply {
                    writeEntityHeader(KEY_NAME, len)
                    writeEntityData(buffer, len)
                }

            } else {
                // 因为文件没有改变，不要备份
                "Don't back up because the file hasn't changed".logD()
                return
            }

            // 使用文件的上次修改时间戳将当前数据的表示形式保存到 newState。
            // 如果您没有将当前数据状态写入该文件，则在下一个回调期间，oldState 将为空。
            val modified = dataFile.lastModified()
            FileOutputStream(newState?.fileDescriptor).also {
                DataOutputStream(it).apply {
                    writeLong(modified)
                }
            }
        } catch (e: IOException) {
            // 无法读取状态文件
        }
    }

    /**
     * 在需要恢复应用数据时，备份管理器会调用备份代理的 onRestore() 方法。备份管理器调用该方法时，会传递您的备份数据，以便您将其恢复到设备上。
     * 只有备份管理器可以调用 onRestore()，当系统安装您的应用并找到现有备份数据时，该调用会自动发生。
     *
     * @param data 一个 BackupDataInput 对象，可让您读取备份数据。
     * @param appVersionCode 一个整数，表示应用的 android:versionCode 清单属性的值，与备份此数据时它所具有的值一样。
     *                       您可以使用此参数来交叉检查当前的应用版本，并确定数据格式是否兼容。
     * @param newState 一个开放的读/写 ParcelFileDescriptor，会指向某个文件。在该文件中，您必须写入随 data 提供的最终备份状态。
     */
    override fun onRestore(
        data: BackupDataInput?,
        appVersionCode: Int,
        newState: ParcelFileDescriptor?
    ) {
        data ?: return
        with(data) {
            while (readNextHeader()) {
                when (key) {
                    KEY_NAME -> {
                        val dataBuf = ByteArray(dataSize).also {
                            readEntityData(it, 0, dataSize)
                        }
                        ByteArrayInputStream(dataBuf).also {
                            DataInputStream(it).apply {
                                name = readUTF()
                                age = readInt()
                            }
                        }
                    }
                    else -> skipEntityData()
                }
            }
        }

        FileOutputStream(newState?.fileDescriptor).also {
            DataOutputStream(it).apply {
                writeUTF(name)
                writeInt(age)
            }
        }
    }
}