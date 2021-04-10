package com.william.easykt.ui

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.graphics.Paint
import android.view.animation.LinearInterpolator
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.dp
import com.william.easykt.databinding.ActivityWaveAnimationBinding


/**
 * @author William
 * @date 2020/9/2 19:16
 * Class Comment：
 */
class WaveAnimationActivity : BaseActivity() {
    override val mViewBinding: ActivityWaveAnimationBinding by bindingView()

    override fun initAction() {

    }

    override fun initData() {
        setTitleText("WaveAnimationActivity")
        mViewBinding.mWaveView.apply {
            setInitialRadius(15f.dp)
            setMaxRadiusRate(1f)
            setDuration(1500)
            setStyle(Paint.Style.FILL)
            setColor(Color.WHITE)
            setSpeed(1000)
            setInterpolator(LinearInterpolator())
            start()
        }

        mViewBinding.start.setOnClickListener {
            mViewBinding.mWaveView.start()
        }

        mViewBinding.stop.setOnClickListener {
            mViewBinding.mWaveView.stop()
        }

        mViewBinding.stopImmediately.setOnClickListener {
            mViewBinding.mWaveView.stopImmediately()
        }

        val animator = ObjectAnimator.ofPropertyValuesHolder(
            mViewBinding.viewOuter,
            PropertyValuesHolder.ofFloat("scaleX", 2f),
            PropertyValuesHolder.ofFloat("scaleY", 2f),
            PropertyValuesHolder.ofFloat("alpha", 0f)
        ).apply {
            repeatCount = ObjectAnimator.INFINITE
            duration = 1000
            start()
        }

        val animator2 = ObjectAnimator.ofPropertyValuesHolder(
            mViewBinding.viewOuter2,
            PropertyValuesHolder.ofFloat("scaleX", 2f),
            PropertyValuesHolder.ofFloat("scaleY", 2f),
            PropertyValuesHolder.ofFloat("alpha", 0f)
        ).apply {
            repeatCount = ObjectAnimator.INFINITE
            duration = 1000
            start()
        }

    }
}