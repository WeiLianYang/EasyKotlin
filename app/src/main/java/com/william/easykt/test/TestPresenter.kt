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