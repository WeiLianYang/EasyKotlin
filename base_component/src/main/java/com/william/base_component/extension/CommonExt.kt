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

package com.william.base_component.extension

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.PersistableBundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.contentValuesOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.william.base_component.BaseApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.IOException

/**
 *  author : WilliamYang
 *  date : 2021/8/12 17:57
 *  description :
 */

val Context.dataStore by preferencesDataStore("easyKt_prefs")

const val APP_DATA_STORE_FILE_NAME = "user_prefs.pb"

suspend fun <T> DataStore<Preferences>.put(key: Preferences.Key<T>, value: T) {
    edit { preferences ->
        preferences[key] = value
    }
}

fun <T> DataStore<Preferences>.get(key: Preferences.Key<T>, defaultVal: T): Flow<T> {
    return data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException) {
            "Error reading preferences, ${exception.message}".logE()
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[key] ?: defaultVal
    }
}

fun String?.getNoNull(default: String = ""): String {
    return this ?: default
}

fun Int.toText(): String = BaseApp.instance.getString(this)

fun Int.toStringArray(): Array<String> = BaseApp.instance.resources.getStringArray(this)

fun Int.toPx(): Int = BaseApp.instance.resources.getDimensionPixelOffset(this)

/**
 * 复制文本到粘贴板
 */
fun Context.copyToClipboard(text: String, label: String = "EasyKt") {
    val clipData = ClipData.newPlainText(label, text)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        clipData.apply {
            description.extras = PersistableBundle().apply {
                putBoolean("android.content.extra.IS_SENSITIVE", true)
            }
        }
    }
    clipboardManager?.setPrimaryClip(clipData)
}

inline fun <reified T : DialogFragment> show(fm: FragmentManager) {
    val clazz = T::class
    val instance = clazz.constructors.first().call()
    instance.show(fm, clazz.simpleName)
}

/**
 * 分享文本到其他应用
 * @param text 分享的文本
 */
fun Activity.shareTextToOtherApp(text: String?) {
    text ?: return
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

/**
 * 分享多张图片给其他应用
 * @param imageUris 分享的图片uri集合
 */
fun Activity.shareImageToOtherApp(imageUris: ArrayList<Uri>?) {
    imageUris ?: return
    imageUris.forEach { uri ->
        val mimeType = this.contentResolver.getType(uri)
        if (mimeType?.contains("image") == false) {
            "只能分享图片".toast()
            return
        }
        val imageName = "${System.currentTimeMillis()}.${
            MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        }"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = contentValuesOf(
                Pair(MediaStore.MediaColumns.DISPLAY_NAME, imageName),
                Pair(MediaStore.MediaColumns.MIME_TYPE, mimeType),
                Pair(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM)
            )
            this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            Uri.fromFile(
                File(this.externalCacheDir!!.absolutePath, imageName)
            )
        }
    }
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND_MULTIPLE
        putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
        type = "image/*"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}
