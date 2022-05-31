package com.william.easykt.ui.anim

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * author：William
 * date：2022/5/29 22:09
 * description：结合 Animation 使用 Camera 做 平移、旋转 效果
 */
class CameraRotateAnimation(
    private val fromDegrees: Float, // 开始角度
    private val endDegree: Float, // 结束角度
    private val reverse: Boolean // 是否反转
) : Animation() {

    /** 最大 Z 轴高度 */
    private val maxDepthZ = 400f

    /** Camera 作用的中心坐标 */
    private var centerX = 0f
    private var centerY = 0f

    /** Camera 对象 */
    private var camera: Camera? = null

    /**
     * 使用动画对象的尺寸以及对象的父对象来初始化此动画
     */
    override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
        super.initialize(width, height, parentWidth, parentHeight)

        camera = Camera()
        centerX = width / 2f
        centerY = height / 2f
    }

    /**
     * @param interpolatedTime 通过插值函数运行后的归一化时间值（0.0 到 1.0）。
     * @param t 用当前变换填充的 Transformation 对象。
     */
    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val matrix = t.matrix
        val degrees = fromDegrees + (endDegree - fromDegrees) * interpolatedTime

        val z = if (reverse) {
            maxDepthZ * interpolatedTime
        } else {
            maxDepthZ * (1.0f - interpolatedTime)
        }

        camera?.apply {
            save()
            translate(0.0f, 0.0f, z)
            rotateY(degrees)
            getMatrix(matrix)
            restore()
        }

        matrix.preTranslate(-centerX, -centerY)
        matrix.postTranslate(centerX, centerY)

        super.applyTransformation(interpolatedTime, t)
    }
}