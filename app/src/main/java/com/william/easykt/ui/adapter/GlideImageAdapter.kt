package com.william.easykt.ui.adapter

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.william.base_component.adapter.BaseRvAdapter
import com.william.base_component.adapter.BaseRvViewHolder
import com.william.base_component.extension.dp
import com.william.base_component.image.*
import com.william.easykt.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 *  author : WilliamYang
 *  date : 2022/8/22 16:42
 *  description :
 */
class GlideImageAdapter constructor(private val activity: AppCompatActivity) :
    BaseRvAdapter<String>() {

    override val layoutResourceId: Int
        get() = R.layout.recycler_item_image

    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int, bean: String?) {
        val imageView = holder.getView<ImageView>(R.id.iv_image)
        when (position) {
            0 -> {
                loadImage(activity, bean, imageView)
            }
            1 -> {
                loadImage(activity, bean, imageView, TYPE_CIRCLE)
            }
            2 -> {
                loadImage(
                    activity, bean, imageView, TYPE_CIRCLE_BORDER,
                    borderWith = 4.dp, borderColor = R.color.color_ff0000
                )
            }
            3 -> {
                loadImage(
                    activity, bean, imageView, TYPE_ROUND_RECT,
                    radius = 8.dp, borderWith = 4.dp, borderColor = R.color.color_ff0000
                )
            }
            4 -> {
                loadImage(
                    activity, bean, imageView, TYPE_RADIUS,
                    radius = 12.dp, cornerType = RoundedCornersTransformation.CornerType.LEFT
                )
            }
            5 -> {
                loadImage(
                    activity, bean, imageView, TYPE_RADIUS_NOT_CROP,
                    radius = 16.dp, cornerType = RoundedCornersTransformation.CornerType.ALL
                )
            }
            6 -> {
                loadImage(
                    activity, bean, imageView, TYPE_BLURRED,
                    blurred = 10, scale = 10
                )
            }
            7 -> {
                loadImage(
                    activity, bean, imageView, TYPE_BLURRED_RADIUS,
                    radius = 16.dp, cornerType = RoundedCornersTransformation.CornerType.ALL,
                    blurred = 10, scale = 2
                )
            }
        }
    }
}