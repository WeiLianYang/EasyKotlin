package com.william.base_component.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter


/**
 * @author William
 * @date 2020/4/27 13:51
 * Class Comment：FragmentStateAdapter base class for many fragments display
 */
open class BaseFragmentStateAdapter(
    activity: FragmentActivity,
    private val fragmentList: MutableList<Fragment>
) :
    FragmentStatePagerAdapter(
        activity.supportFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

}