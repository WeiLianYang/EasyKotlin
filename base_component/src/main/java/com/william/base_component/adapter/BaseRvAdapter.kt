/*
 * Copyright WeiLianYang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.william.base_component.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.william.base_component.adapter.BaseRvViewHolder.Companion.create
import com.william.base_component.alias.OnAdapterItemChildClick
import com.william.base_component.alias.OnAdapterItemChildLongClick
import com.william.base_component.alias.OnAdapterItemClick
import com.william.base_component.alias.OnAdapterItemLongClick
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * @author William
 * @date 2020-02-17 17:38
 * Class Comment：RecyclerView adapter base class
 */
abstract class BaseRvAdapter<T> constructor() : RecyclerView.Adapter<BaseRvViewHolder>() {
    protected var mActivity: Activity? = null
    protected var mFragment: Fragment? = null
    private var mList: MutableList<T> = ArrayList()
    private var mOnItemClickListener: OnAdapterItemClick<T>? = null
    private var mOnItemLongClickListener: OnAdapterItemLongClick<T>? = null
    private var mOnItemChildClickListener: OnAdapterItemChildClick<T>? = null
    private var mOnItemChildLongClickListener: OnAdapterItemChildLongClick<T>? = null

    constructor(activity: Activity?) : this() {
        mActivity = activity
    }

    constructor(fragment: Fragment) : this(fragment.activity) {
        mFragment = fragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder {
        return create(mActivity, mFragment, inflateView(layoutResourceId, parent))
    }

    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.invoke(
                holder,
                position,
                if (position >= mList.size) null else mList[position]
            )
        }
        holder.itemView.setOnLongClickListener {
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener?.invoke(
                    holder,
                    position,
                    if (position >= mList.size) null else mList[position]
                )
                return@setOnLongClickListener true
            }
            false
        }
        onBindViewHolder(
            holder,
            position,
            if (position >= mList.size) null else mList[position]
        )
    }

    @get:LayoutRes
    protected abstract val layoutResourceId: Int

    /**
     * 绑定holder
     *
     * @param holder   h
     * @param position p
     * @param bean     b
     */
    protected abstract fun onBindViewHolder(holder: BaseRvViewHolder, position: Int, bean: T?)

    override fun getItemCount(): Int {
        return mList.size
    }

    /**
     * 通过布局ID获取View
     *
     * @param layoutResId l
     * @param parent      p
     * @return r
     */
    protected fun inflateView(@LayoutRes layoutResId: Int, parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
    }

    /**
     * 获取该适配器的列表数据
     *
     * @return l
     */
    val data: List<T>?
        get() = mList

    operator fun get(position: Int): T? {
        return if (position < 0 || position >= itemCount) {
            null
        } else mList[position]
    }

    operator fun set(position: Int, bean: T) {
        if (position < 0 || position >= itemCount) {
            return
        }
        mList[position] = bean
        notifyItemChanged(position)
    }

    fun add(bean: T?) {
        if (bean != null) {
            mList.add(bean)
        }
    }

    fun add(position: Int, bean: T?) {
        if (position < 0 || position > itemCount) {
            return
        }
        if (bean != null) {
            mList.add(position, bean)
        }
    }

    fun addAll(beans: List<T>?) {
        if (beans != null) {
            mList.addAll(beans)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(beans: List<T>?) {
        if (beans != null) {
            mList.clear()
            mList.addAll(beans)
            notifyDataSetChanged()
        }
    }

    fun remove(position: Int): T? {
        return if (position >= 0 && position < mList.size) {
            mList.removeAt(position)
        } else null
    }

    fun remove(t: T?): Boolean {
        return if (t != null) {
            mList.remove(t)
        } else false
    }

    fun clear() {
        if (mList.isNotEmpty()) {
            mList.clear()
        }
    }

    fun clearAndNotifyDataSetChanged() {
        if (mList.isNotEmpty()) {
            mList.clear()
        }
        notifyDataSetChanged()
    }

    val isEmpty: Boolean
        get() = mList.isEmpty()

    val isNotEmpty: Boolean
        get() = mList.isNotEmpty()

    /**
     * 添加子项View到点击事件中
     *
     * @param viewId   控件Id
     * @param holder   BaseRvViewHolder
     * @param position 当前位置
     * @param bean     数据
     */
    @SuppressLint("CheckResult")
    protected fun addOnChildClickListener(
        @IdRes viewId: Int,
        holder: BaseRvViewHolder,
        position: Int,
        bean: T
    ) {
        val v = holder.getView<View>(viewId)
        v?.clicks()
            ?.throttleFirst(800, TimeUnit.MILLISECONDS)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                mOnItemChildClickListener?.invoke(v, holder, position, bean)
            }
    }

    /**
     * 添加子项View到长按事件中
     *
     * @param viewId   控件Id
     * @param holder   BaseRvViewHolder
     * @param position 当前位置
     * @param bean     数据
     */
    protected fun addOnChildLongClickListener(
        @IdRes viewId: Int,
        holder: BaseRvViewHolder,
        position: Int,
        bean: T
    ) {
        holder.getView<View>(viewId)?.setOnLongClickListener { v: View ->
            mOnItemChildLongClickListener?.invoke(v, holder, position, bean)
            true
        }
    }

    fun setOnItemClickListener(listener: OnAdapterItemClick<T>): BaseRvAdapter<T> {
        mOnItemClickListener = listener
        return this
    }

    fun setOnItemLongClickListener(listener: OnAdapterItemLongClick<T>): BaseRvAdapter<T> {
        mOnItemLongClickListener = listener
        return this
    }

    fun setOnItemChildClickListener(listener: OnAdapterItemChildClick<T>): BaseRvAdapter<T> {
        mOnItemChildClickListener = listener
        return this
    }

    fun setOnItemChildLongClickListener(listener: OnAdapterItemChildLongClick<T>): BaseRvAdapter<T> {
        mOnItemChildLongClickListener = listener
        return this
    }

}