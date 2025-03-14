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

package com.william.easykt

import coil.ImageLoader
import coil.ImageLoaderFactory
import com.william.base_component.BaseApp
import com.william.toolkit.ToolkitPanel
import com.william.toolkit.bean.ToolkitConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider

/**
 *  author : WilliamYang
 *  date : 2021/12/14 15:23
 *  description :
 */
@HiltAndroidApp
class App : BaseApp(), ImageLoaderFactory {

    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    override fun newImageLoader(): ImageLoader = imageLoader.get()

    override fun onCreate() {
        super.onCreate()
        val config = ToolkitConfig.Builder().setDebugMode(BuildConfig.DEBUG).build()
        ToolkitPanel.init(this, config)
    }

}