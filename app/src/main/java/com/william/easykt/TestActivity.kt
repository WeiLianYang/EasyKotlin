package com.william.easykt

import android.content.Context
import android.content.Intent
import com.william.base_component.mvp.BaseMvpActivity


/**
 * @author William
 * @date 2020/5/3 15:49
 * Class Comment：
 */
class TestActivity : BaseMvpActivity<TestPresenter>() {

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initData() {
        setTitleText(R.string.album_title)

        val strList = listOf("a", "b", "c")

    }

    override val layoutId: Int
        get() = R.layout.activity_test

    companion object {

        @JvmStatic
        fun startTarget(context: Context) {
            val intent = Intent(context, TestActivity::class.java)
            context.startActivity(intent)
        }
    }
}