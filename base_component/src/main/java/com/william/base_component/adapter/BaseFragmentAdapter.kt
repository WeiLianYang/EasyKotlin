package com.william.base_component.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter


/**
 * @author William
 * @date 2020/4/27 13:51
 * Class Comment：FragmentAdapter base class
 */
open class BaseFragmentAdapter(
    activity: FragmentActivity,
    private val fragmentList: MutableList<Fragment>
) :
    FragmentPagerAdapter(activity.supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

}