package com.william.easykt.test

import com.william.base_component.mvp.base.BasePresenter
import com.william.base_component.rx.BaseObserver
import com.william.base_component.rx.SchedulerUtils
import com.william.easykt.data.Banner
import com.william.easykt.data.HttpResponse


/**
 * @author William
 * @date 2020/5/3 15:49
 * Class Comment：
 */
class TestPresenter : BasePresenter<TestContract.IView, TestModel>(), TestContract.IPresenter {

    override fun getBanners() {
        model?.getBanners()?.compose(SchedulerUtils.ioToMain())
            ?.subscribe(object : BaseObserver<HttpResponse<List<Banner>>>() {
                override fun onSuccess(entity: HttpResponse<List<Banner>>?) {
                    view?.setupData(entity)
                }

                override fun onFail(code: Int?, msg: String?) {

                }
            })
    }

}