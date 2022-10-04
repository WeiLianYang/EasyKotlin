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

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import com.william.base_component.extension.logD
import com.william.easykt.utils.MediaPickerParams.Companion.SELECT_PHOTO
import com.william.easykt.utils.MediaPickerParams.Companion.SELECT_VIDEO


/**
 * 选择照片的协定
 * Input type  : MediaPickerParams 不需要传值
 * Output type : List<Uri> 选择完成后的 image uri
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class MediaContract : ActivityResultContract<MediaPickerParams, List<Uri>>() {

    private var limit = 1

    @CallSuper
    override fun createIntent(context: Context, input: MediaPickerParams): Intent {
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        if (input.type == SELECT_PHOTO) {
            intent.type = "image/*"
        } else if (input.type == SELECT_VIDEO) {
            intent.type = "video/*"
        }

        limit = input.limit
        val supportMax = MediaStore.getPickImagesMaxLimit()
        if (limit > supportMax) {
            limit = supportMax
        }
        if (limit > 1) {
            intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, limit)
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        val list = arrayListOf<Uri>()
        if (limit == 1) {
            intent?.data?.let {
                list.add(it)
            }
        } else {
            val size = intent?.clipData?.itemCount ?: 0
            for (i in 0 until size) {
                intent?.clipData?.getItemAt(i)?.uri?.let {
                    list.add(it)
                }
            }
        }
        "media pick list: $list".logD()
        return list
    }
}

data class MediaPickerParams(
    /** 媒体选择类型 **/
    val type: Int = SELECT_PHOTO_VIDEO,

    /** 选择的最大数量，默认为1，单选 **/
    val limit: Int = 1
) {

    companion object {

        /** 既可以选择照片也可以选择视频 **/
        const val SELECT_PHOTO_VIDEO = 0

        /** 只可以选择照片 **/
        const val SELECT_PHOTO = 1

        /** 只可以选择视频 **/
        const val SELECT_VIDEO = 2
    }
}
