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

import android.util.Base64

/**
 * 字节数组 转 base64
 * @param data
 * @return base64 of byte array
 */
fun bytesToBase64(data: ByteArray?): String {
    data ?: return ""
    return Base64.encodeToString(data, Base64.DEFAULT)
}

/**
 * base64 转 字节数组
 * @param data
 * @return byte array from base64
 */
fun base64ToBytes(data: String?): ByteArray {
    data ?: return byteArrayOf()
    return Base64.decode(data, Base64.DEFAULT)
}