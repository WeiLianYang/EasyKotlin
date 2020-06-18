package com.william.base_component.utils

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.RequestCallback


/**
 * @author William
 * @date 2020/5/25 16:27
 * Class Comment：权限工具类
 */

/**
 * 检查权限是否已同意
 */
fun checkPermission(
    activity: FragmentActivity,
    vararg permissions: String
): Boolean {
    for (per in permissions) {
        val hasPermission = ActivityCompat.checkSelfPermission(
            activity,
            per
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) return false
    }
    return true
}

/**
 * 检查权限并申请
 */
fun checkPermissionAndRequest(
    activity: FragmentActivity,
    vararg permissions: String,
    importantPermissions: MutableList<String>? = null,
    importantMessage: String = "该权限是程序必须依赖的权限",
    message: String = "为了保证程序正常工作，请您同意以下权限申请",
    positiveText: String = "好的",
    callback: RequestCallback
) {
    PermissionX.init(activity)
        .permissions(*permissions)
        .explainReasonBeforeRequest()
        .onExplainRequestReason { deniedList, beforeRequest ->
            if (beforeRequest) {
                // 在请求权限之前解释权限申请原因
                showRequestReasonDialog(deniedList, message, positiveText)
            } else {
                // 当有多个权限被拒绝时，我们只重新申请importantPermission权限。
                val filteredList = deniedList.filter {
                    // 在拒绝的权限中筛选当前申请的比较重要的权限，希望用户能授予
                    importantPermissions?.contains(it) ?: false
                }
                showRequestReasonDialog(filteredList, importantMessage, positiveText)
            }
        }
        .onForwardToSettings { deniedList ->
            // 所有被用户选择了拒绝且不再询问的权限都会进行到这个方法中处理，拒绝的权限都记录在deniedList参数当中。
            val newList = deniedList.filter {
                // 在拒绝的权限中筛选当前申请的比较重要的权限，希望用户能去设置中开启
                importantPermissions?.contains(it) ?: false
            }
            showForwardToSettingsDialog(newList, "您需要去应用程序设置当中手动开启权限", positiveText, "取消")
        }.request { allGranted, grantedList, deniedList ->
            // 执行后续具体操作
            callback.invoke(allGranted, grantedList, deniedList)
        }
}
