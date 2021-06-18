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

package com.william.easykt.test

import com.william.base_component.mvp.base.IBaseView
import com.william.base_component.net.data.BaseResponse
import com.william.easykt.data.Banner
import io.reactivex.Observable

interface TestContract {
    interface IView : IBaseView {

        fun setupData(response: List<Banner>?)
    }

    interface IPresenter {

        fun getBanners()

    }

    interface IModel {

        fun getBanners(): Observable<BaseResponse<List<Banner>>>

    }
}