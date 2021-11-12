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

package com.william.easykt.ui

import android.Manifest
import com.orhanobut.logger.Logger
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.utils.requestPermission
import com.william.easykt.databinding.ActivityPermissionBinding


/**
 * @author WilliamYang
 * @date 2021/2/19 13:22
 * Class Comment：权限
 */
class PermissionActivity : BaseActivity() {

    override val viewBinding: ActivityPermissionBinding by bindingView()

    override fun initAction() {
    }

    override fun initData() {
        setTitleText(javaClass.simpleName)

        viewBinding.tvButton.setOnClickListener {
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