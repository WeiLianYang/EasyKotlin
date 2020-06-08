package com.william.base_component.utils

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import com.william.base_component.alias.OnPermissionCallback


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
    importantPermission: String? = null,
    importantMessage: String = "该权限是程序必须依赖的权限",
    message: String = "为了保证程序正常工作，请您同意以下权限申请",
    positiveText: String = "好的",
    callback: OnPermissionCallback
) {
    PermissionX.init(activity)
        .permissions(*permissions)
        .explainReasonBeforeRequest()
        .onExplainRequestReason { deniedList, beforeRequest ->
            if (beforeRequest) {
                showRequestReasonDialog(deniedList, message, positiveText)
            } else {
                val filteredList = deniedList.filter {
                    it == android.Manifest.permission.CAMERA
                }
                showRequestReasonDialog(filteredList, importantMessage, positiveText)
            }
        }.onForwardToSettings { deniedList ->
            val newList = deniedList.filter {
                it == importantPermission
            }
            showForwardToSettingsDialog(newList, "您需要去应用程序设置当中手动开启权限", positiveText, "取消")
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                callback.invoke()
            } else {
                Toast.makeText(activity, "您拒绝了权限申请", Toast.LENGTH_SHORT).show()
            }
        }
}