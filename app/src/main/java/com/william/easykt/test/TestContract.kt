package com.william.easykt.test

import com.william.base_component.mvp.base.IBaseView
import com.william.easykt.data.Banner
import com.william.easykt.data.HttpResponse
import io.reactivex.Observable

interface TestContract {
    interface IView : IBaseView {

        fun setupData(response: HttpResponse<List<Banner>>?)
    }

    interface IPresenter {

        fun getBanners()

    }

    interface IModel {

        fun getBanners(): Observable<HttpResponse<List<Banner>>>

    }
}