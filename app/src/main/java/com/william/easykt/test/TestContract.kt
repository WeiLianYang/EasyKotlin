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