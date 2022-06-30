package com.william.easykt.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.william.base_component.extension.logD
import com.william.base_component.extension.logV
import com.william.easykt.R
import java.util.*

/**
 * 自定义闹钟视图
 */
class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /** 闹钟遮罩位图 */
    private var clockMaskBitmap: Bitmap? = null

    /** 闹钟位图 */
    private var clockBitmap: Bitmap? = null

    /** 画笔 */
    private var paint: Paint? = null

    /** 画笔混合模式 */
    private var xfermode: Xfermode? = null

    /** 闹钟矩形 */
    private var clockRect: RectF? = null

    /** 闹钟视图旋转角度 */
    private var angle = 0f

    /** 动画对象 */
    private var animator: ValueAnimator? = null

    /** 初始角度 */
    private var initAngle = 0f

    /** 时间对象 */
    private var calendar: Calendar? = null

    /** 时间字体大小 */
    private var textSize = 0

    /** 时间字体颜色 */
    private var textColor = 0

    /** 时间文本起始 x 坐标 */
    private var timeTextStartX = 0

    /** 时间文本起始 y 坐标 */
    private var timeTextStartY = 0

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.ClockView
        )
        textSize = typedArray.getDimensionPixelSize(
            R.styleable.ClockView_clockTextSize, 14
        )
        textColor = typedArray.getColor(
            R.styleable.ClockView_clockTextColor, Color.RED
        )
        typedArray.recycle()
        init()
    }

    private fun init() {
        clockMaskBitmap = BitmapFactory.decodeResource(resources, R.drawable.clock_mask)
        clockBitmap = BitmapFactory.decodeResource(resources, R.drawable.clock)

        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        paint = Paint(Paint.ANTI_ALIAS_FLAG)

        clockRect = RectF()
        val defaultText = "00:00:00"
        val timeTextRect = Rect()
        paint?.textSize = textSize.toFloat()
        paint?.getTextBounds(defaultText, 0, defaultText.length, timeTextRect)
        "timeTextRect: $timeTextRect".logD()
        timeTextStartX = (clockBitmap?.width ?: 0) / 2
        timeTextStartY = (clockBitmap?.height ?: 0) / 2 - timeTextRect.centerY()

        calendar = Calendar.getInstance()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(clockBitmap?.width ?: 0, clockBitmap?.height ?: 0)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(clockBitmap?.width ?: 0, heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, clockBitmap?.height ?: 0)
        } else {
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        clockRect?.set(
            0f, 0f, clockBitmap?.width?.toFloat() ?: 0f, clockBitmap?.height?.toFloat() ?: 0f
        )
        @Suppress("DEPRECATION")
        canvas.saveLayer(clockRect, paint, Canvas.ALL_SAVE_FLAG)
        canvas.drawBitmap(clockBitmap!!, 0f, 0f, paint)
        paint?.xfermode = xfermode
        //旋转画布
        canvas.rotate(
            angle, (clockBitmap?.width ?: 0) / 2f, (clockBitmap?.height ?: 0) / 2f
        )
        canvas.drawBitmap(clockMaskBitmap!!, 0f, 0f, paint)
        paint?.xfermode = null
        canvas.restore()

        updateTimeText(canvas)
    }

    /** 更新时间 */
    private fun updateTimeText(canvas: Canvas) {
        val currentTimeMillis = System.currentTimeMillis()
        calendar?.timeInMillis = currentTimeMillis
        // 格式化时间
        @SuppressLint("DefaultLocale") val timeStr = String.format(
            "%02d:%02d:%02d",
            calendar?.get(Calendar.HOUR_OF_DAY),
            calendar?.get(Calendar.MINUTE),
            calendar?.get(Calendar.SECOND)
        )
        paint?.color = textColor
        paint?.textAlign = Paint.Align.CENTER
        canvas.drawText(
            timeStr, timeTextStartX.toFloat(), timeTextStartY.toFloat(), paint!!
        )
    }

    /** 执行动画 */
    fun performAnimation() {
        if (animator?.isRunning == true) {
            return
        }
        animator = ValueAnimator.ofFloat(0f, 360f).apply {
            addUpdateListener { animation: ValueAnimator ->
                angle = animation.animatedValue as Float + initAngle
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    calendar?.timeInMillis = System.currentTimeMillis()
                    initAngle = (calendar?.get(Calendar.SECOND) ?: 0) * (360 / 60f) // 每秒6度
                }
            })
            duration = MINUTE.toLong()
            interpolator = LinearInterpolator()
            repeatCount = Animation.INFINITE
            start()
        }
    }

    companion object {
        const val MINUTE = 60 * 1000
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        "clock view detached from window".logV()
    }
}