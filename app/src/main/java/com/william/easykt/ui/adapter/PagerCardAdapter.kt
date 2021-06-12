package com.william.easykt.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.william.easykt.R


/**
 * @author WilliamYang
 * @date 2021/2/18 20:19
 * Class Comment：卡片ViewPager适配器
 */
class PagerCardAdapter : PagerAdapter() {

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun getCount(): Int = Int.MAX_VALUE

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View = LayoutInflater.from(container.context)
            .inflate(R.layout.item_pager_card, container, false)
        container.addView(view)
        return view
    }
}