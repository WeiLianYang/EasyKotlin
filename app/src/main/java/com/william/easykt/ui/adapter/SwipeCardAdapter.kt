package com.william.easykt.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.william.easykt.R


/**
 * @author William
 * @date 2020/6/28 14:09
 * Class Comment：滑动卡片适配器
 */
class SwipeCardAdapter : PagerAdapter() {

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun getCount(): Int = Int.MAX_VALUE

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(container.context)
            .inflate(R.layout.item_swipe_card, container, false)
        val mTvPosition = view.findViewById<TextView>(R.id.tv_position)
        mTvPosition.text = "position-> $position"
        container.addView(view)
        return view
    }
}