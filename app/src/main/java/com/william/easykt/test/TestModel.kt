package com.william.easykt.test

import com.william.base_component.mvp.base.BaseModel
import com.william.easykt.data.Banner
import com.william.easykt.data.HttpResponse
import com.william.easykt.service.Api
import io.reactivex.Observable


/**
 * @author William
 * @date 2020/5/3 15:52
 * Class Comment：
 */
class TestModel : BaseModel(), TestContract.IModel {

    override fun getBanners(): Observable<HttpResponse<List<Banner>>> {
        return Api.service.getBanners()
    }

}