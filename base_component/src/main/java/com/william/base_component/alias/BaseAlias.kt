package com.william.base_component.alias

import android.graphics.drawable.Drawable
import android.view.View
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.william.base_component.adapter.BaseRvViewHolder
import com.william.base_component.net.data.BaseResponse


/**
 * @author William
 * @date 2020/4/22 17:04
 * Class Comment：类型别名
 */

/**
 * Network request-response successful callback function
 * Callback for [com.william.base_component.rx.BaseObserver.onSuccess]
 * {@link BaseObserver}
 * @param T data
 */
typealias SuccessCallback<T> = (response: BaseResponse<T>) -> Unit

/**
 * Network request-response failed callback function
 * Callback for [com.william.base_component.rx.BaseObserver.onFail]
 */
typealias FailCallback = (code: Int?, msg: String?) -> Unit

/**
 * The item of BaseAdapter clicks the event callback function
 * Callback for [com.william.base_component.adapter.BaseRvAdapter.mOnItemClickListener]
 * @param T item bean
 */
typealias OnAdapterItemClick<T> = (holder: BaseRvViewHolder, position: Int, bean: T?) -> Unit

/**
 * Item of BaseAdapter long press event callback function
 * Callback for [com.william.base_component.adapter.BaseRvAdapter.mOnItemLongClickListener]
 * @param T item bean
 */
typealias OnAdapterItemLongClick<T> = (holder: BaseRvViewHolder, position: Int, bean: T?) -> Unit

/**
 * The item sub item of BaseAdapter clicks the event callback function
 * Callback for [com.william.base_component.adapter.BaseRvAdapter.mOnItemChildClickListener]
 * @param T item bean
 */
typealias OnAdapterItemChildClick<T> = (view: View, holder: BaseRvViewHolder, position: Int, bean: T?) -> Unit

/**
 * The item sub item of BaseAdapter long presses the event callback function
 * Callback for [com.william.base_component.adapter.BaseRvAdapter.mOnItemChildLongClickListener]
 * @param T item bean
 */
typealias OnAdapterItemChildLongClick<T> = (view: View, holder: BaseRvViewHolder, position: Int, bean: T?) -> Unit

/**
 * Glide's image loads the callback function
 * Callback for [com.william.base_component.image.GlideLoader.load]
 */
typealias OnGlideResourceReady = (resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean) -> Unit

/**
 * Glide's image load failed callback function
 * Callback for [com.william.base_component.image.GlideLoader.load]
 */
typealias OnGlideLoadFailed = (e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean) -> Unit
