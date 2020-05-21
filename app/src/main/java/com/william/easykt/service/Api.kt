package com.william.easykt.service

import com.william.base_component.net.RetrofitHelper


/**
 * @author William
 * @date 2020/5/19 13:19
 * Class Comment：Api
 */
object Api {

    val service = RetrofitHelper.create(ApiService::class.java)

}