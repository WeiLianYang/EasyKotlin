package com.android.debugtools.base

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.debugtools.base.BaseRvViewHolder.Companion.create
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * @author William
 * @date 2020-02-17 17:38
 * Class Comment：通用适配器
 */
abstract class BaseRvAdapter<T> : RecyclerView.Adapter<BaseRvViewHolder> {
    protected var mActivity: Activity? = null
    protected var mFragment: Fragment? = null
    protected var mList: MutableList<T>? = null
    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null
    private var mOnItemChildClickListener: OnItemChildClickListener<T>? = null
    private var mOnItemChildLongClickListener: OnItemChildLongClickListener<T>? = null

    constructor() {
        mList = ArrayList()
    }

    constructor(activity: Activity?) : this() {
        mActivity = activity
    }

    constructor(fragment: Fragment) : this(fragment.activity) {
        mFragment = fragment
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder {
        return create(mActivity, mFragment, inflateView(getLayoutResourceId(), parent))
    }

    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int) {
        onBindViewHolder(
            holder,
            position,
            if (position >= mList!!.size) null else mList!![position]
        )
        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(
                holder,
                position,
                if (position >= mList!!.size) null else mList!![position]
            )
        }
        holder.itemView.setOnLongClickListener {
            if (mOnItemLongClickListener != null) {
                mOnItemLongClickListener?.onItemLongClick(
                    holder,
                    position,
                    if (position >= mList!!.size) null else mList!![position]
                )
                return@setOnLongClickListener true
            }
            false
        }
    }

    /**
     * 布局id
     *
     * @return r
     */
    protected abstract fun getLayoutResourceId(): Int

    /**
     * 绑定holder
     *
     * @param holder   h
     * @param position p
     * @param bean     b
     */
    protected open fun onBindViewHolder(holder: BaseRvViewHolder, position: Int, bean: T?) {

    }

    override fun getItemCount(): Int {
        return if (mList != null) mList!!.size else 0
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
        } else mList!![position]
    }

    operator fun set(position: Int, bean: T) {
        if (position < 0 || position >= itemCount) {
            return
        }
        mList!![position] = bean
        notifyItemChanged(position)
    }

    fun add(bean: T?) {
        if (bean != null) {
            mList!!.add(bean)
        }
    }

    fun add(position: Int, bean: T?) {
        if (position < 0 || position > itemCount) {
            return
        }
        if (bean != null) {
            mList!!.add(position, bean)
        }
    }

    fun addAll(beans: List<T>?) {
        if (beans != null) {
            mList!!.addAll(beans)
        }
    }

    fun remove(position: Int): T? {
        return if (position >= 0 && position < mList!!.size) {
            mList!!.removeAt(position)
        } else null
    }

    fun remove(t: T?): Boolean {
        return if (t != null) {
            mList!!.remove(t)
        } else false
    }

    fun clear() {
        if (mList!!.isNotEmpty()) {
            mList!!.clear()
        }
    }

    fun clearAndNotifyDataSetChanged() {
        if (mList!!.isNotEmpty()) {
            mList!!.clear()
        }
        notifyDataSetChanged()
    }

    val isEmpty: Boolean
        get() = mList!!.isEmpty()

    val isNotEmpty: Boolean
        get() = mList!!.isNotEmpty()

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
                mOnItemChildClickListener?.onItemChildCLick(v, holder, position, bean)
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
    @Suppress("unused")
    protected fun addOnChildLongClickListener(
        @IdRes viewId: Int,
        holder: BaseRvViewHolder,
        position: Int,
        bean: T
    ) {
        holder.getView<View>(viewId)?.setOnLongClickListener { v: View ->
            mOnItemChildLongClickListener?.onItemChildLongClick(v, holder, position, bean)
            true
        }
    }

    fun setOnItemClickListener(OnItemClickListener: OnItemClickListener<T>): BaseRvAdapter<T> {
        mOnItemClickListener = OnItemClickListener
        return this
    }

    fun setOnItemLongClickListener(OnItemLongClickListener: OnItemLongClickListener<T>): BaseRvAdapter<T> {
        mOnItemLongClickListener = OnItemLongClickListener
        return this
    }

    fun setOnItemChildClickListener(OnItemChildClickListener: OnItemChildClickListener<T>): BaseRvAdapter<T> {
        mOnItemChildClickListener = OnItemChildClickListener
        return this
    }

    @Suppress("unused")
    fun setOnItemChildLongClickListener(OnItemChildLongClickListener: OnItemChildLongClickListener<T>): BaseRvAdapter<T> {
        mOnItemChildLongClickListener = OnItemChildLongClickListener
        return this
    }

    interface OnItemClickListener<T> {
        /**
         * 条目点击事件
         *
         * @param holder   h
         * @param position p
         * @param bean     b
         */
        fun onItemClick(holder: BaseRvViewHolder, position: Int, bean: T?)
    }

    interface OnItemLongClickListener<T> {
        /**
         * 条目长按事件
         *
         * @param holder   h
         * @param position p
         * @param bean     b
         */
        fun onItemLongClick(holder: BaseRvViewHolder, position: Int, bean: T?)
    }

    interface OnItemChildClickListener<T> {
        /**
         * 条目子项点击事件
         *
         * @param view     v
         * @param holder   h
         * @param position p
         * @param bean     b
         */
        fun onItemChildCLick(view: View, holder: BaseRvViewHolder, position: Int, bean: T)
    }

    interface OnItemChildLongClickListener<T> {
        /**
         * 条目子项长按事件
         *
         * @param view     v
         * @param holder   h
         * @param position p
         * @param bean     b
         */
        fun onItemChildLongClick(view: View, holder: BaseRvViewHolder, position: Int, bean: T)
    }

}