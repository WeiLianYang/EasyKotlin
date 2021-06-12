package com.william.easykt.ui

import android.Manifest
import com.orhanobut.logger.Logger
import com.william.base_component.activity.BaseActivity
import com.william.base_component.utils.requestPermission
import com.william.easykt.databinding.ActivityPermissionBinding


/**
 * @author WilliamYang
 * @date 2021/2/19 13:22
 * Class Comment：权限
 */
class PermissionActivity : BaseActivity() {

    override val mViewBinding: ActivityPermissionBinding by bindingView()

    override fun initAction() {
    }

    override fun initData() {
        setTitleText(javaClass.simpleName)

        mViewBinding.tvButton.setOnClickListener {
            requestPermission(
                this,
                mutableListOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                importantPermissions = mutableListOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) { allGranted, grantedList, deniedList ->
                Logger.d(
                    "allGranted: $allGranted, " +
                            "grantedList: $grantedList, " +
                            "deniedList: $deniedList"
                )
            }
        }
    }
}