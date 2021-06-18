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

package com.william.base_component.utils

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import com.william.base_component.alias.RequestCallback


/**
 * @author William
 * @date 2020/5/25 16:27
 * Class Comment：权限工具类
 */

/**
 * 检查一组权限是否已同意
 */
fun checkPermissionArray(
    context: Context,
    vararg permissions: String
): Boolean {
    for (per in permissions) {
        if (!checkPermission(context, per)) return false
    }
    return true
}

/**
 * 检查某个权限是否已同意
 */
fun checkPermission(
    context: Context,
    permission: String
): Boolean {
    return PermissionX.isGranted(context, permission)
}

/**
 * 申请权限
 * 1. 在申请之前，先解释需要申请权限的原因
 * 2. 当有多个权限被拒绝时，我们只重新申请importantPermission权限
 * 3. 如果importantPermission仍旧被拒绝，则提示用户去设置中手动开启
 * 4. 当应用回到前台时，回调申请结果
 */
fun requestPermission(
    activity: FragmentActivity,
    permissions: MutableList<String>,
    importantPermissions: MutableList<String>? = null,
    importantMessage: String = "该权限是程序必须依赖的权限",
    message: String = "为了保证程序正常工作，请您同意以下权限申请",
    positiveText: String = "好的",
    callback: RequestCallback
) {
    PermissionX.init(activity)
        .permissions(permissions)
        .explainReasonBeforeRequest()
        .onExplainRequestReason { scope, deniedList, beforeRequest ->
            if (beforeRequest) {
                // 在请求权限之前解释权限申请原因
                scope.showRequestReasonDialog(deniedList, message, positiveText)
            } else {
                // 当有多个权限被拒绝时，我们只重新申请importantPermission权限。
                val filteredList = deniedList.filter {
                    // 在拒绝的权限中筛选当前申请的比较重要的权限，希望用户能授予
                    importantPermissions?.contains(it) ?: false
                }
                scope.showRequestReasonDialog(filteredList, importantMessage, positiveText)
            }
        }
        .onForwardToSettings { scope, deniedList ->
            // 所有被用户选择了拒绝且不再询问的权限都会进行到这个方法中处理，拒绝的权限都记录在deniedList参数当中。
            val newList = deniedList.filter {
                // 在拒绝的权限中筛选当前申请的比较重要的权限，希望用户能去设置中开启
                importantPermissions?.contains(it) ?: false
            }
            scope.showForwardToSettingsDialog(newList, "您需要去应用程序设置当中手动开启权限", positiveText, "取消")
        }.request { allGranted, grantedList, deniedList ->
            // 执行后续具体操作
            callback.invoke(allGranted, grantedList, deniedList)
        }
}
