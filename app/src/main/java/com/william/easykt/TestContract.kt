package com.william.easykt

import com.william.base_component.mvp.base.IBaseModel
import com.william.base_component.mvp.base.IBaseView

interface TestContract {
    interface IView : IBaseView {

        fun setupData()
    }

    interface IPresenter {

        fun getData()
    }

    interface IModel : IBaseModel {

        fun getData()
    }
}