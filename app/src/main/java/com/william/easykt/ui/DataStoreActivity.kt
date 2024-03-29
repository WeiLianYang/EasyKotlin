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

import android.content.Context
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.APP_DATA_STORE_FILE_NAME
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.dataStore
import com.william.easykt.R
import com.william.easykt.UserPreferences
import com.william.easykt.data.repo.PreferencesRepository
import com.william.easykt.data.repo.UserPreferencesRepository
import com.william.easykt.databinding.ActivityDatastoreBinding
import com.william.easykt.datastore.serializer.UserPreferencesSerializer
import com.william.easykt.viewmodel.DataStoreViewModel
import com.william.easykt.viewmodel.provideFactory

// Build the DataStore
private val Context.userPreferencesStore: DataStore<UserPreferences> by dataStore(
    fileName = APP_DATA_STORE_FILE_NAME,
    serializer = UserPreferencesSerializer()
)

/**
 * @author William
 * @date 2022/8/22 21:35
 * Class Comment：Data Store demo
 */
class DataStoreActivity : BaseActivity() {

    override val viewBinding: ActivityDatastoreBinding by bindingView()

    private val viewModel: DataStoreViewModel by viewModels {
        provideFactory<DataStoreViewModel>(
            PreferencesRepository(dataStore),
            UserPreferencesRepository(userPreferencesStore)
        )
    }

    override fun initView() {
        super.initView()

        viewBinding.button1.setOnClickListener {
            viewModel.savePreferencesData()
        }

        viewBinding.button2.setOnClickListener {
            viewModel.saveUserPreferencesData()
        }
    }

    override fun initData() {
        setTitleText(R.string.test_datastore)

        viewModel.preferencesData.observe(this) {
            viewBinding.text1.text = it
        }

        viewModel.userPreferencesData.observe(this) {
            viewBinding.text2.text = it.toString()
        }
    }

}