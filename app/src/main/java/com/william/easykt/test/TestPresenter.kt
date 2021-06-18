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

import com.william.base_component.mvp.base.BasePresenter
import com.william.base_component.rx.BaseObserver
import com.william.base_component.rx.ioToMain


/**
 * @author William
 * @date 2020/5/3 15:49
 * Class Comment：
 */
class TestPresenter : BasePresenter<TestContract.IView, TestModel>(), TestContract.IPresenter {

    override fun getBanners() {
        model?.getBanners()?.compose(ioToMain())
            ?.subscribe(
                BaseObserver(
                    this, showLoading = true,
                    onSuccess = {
                        view?.setupData(it.data)
                    }, onFail = { code: Int?, msg: String? ->

                    })
            )
    }

}