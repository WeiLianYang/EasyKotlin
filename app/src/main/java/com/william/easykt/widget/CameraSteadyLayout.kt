package com.william.easykt.widget

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.BounceInterpolator
import androidx.constraintlayout.widget.ConstraintLayout

/**
 * author：William
 * date：2022/6/3 19:35
 * description：camera steady animation
 */
class CameraSteadyLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var centerX = 0
    private var centerY = 0
    private var rotateX = 0f
    private var rotateY = 0f

    private var steadyAnimator: ValueAnimator? = null
    private val matrixCanvas = Matrix()
    private val camera = Camera()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
    }

    override fun dispatchDraw(canvas: Canvas) {
        matrixCanvas.reset()
        camera.save()
        camera.rotateX(rotateX)
        camera.rotateY(rotateY)
        camera.getMatrix(matrixCanvas)
        camera.restore()

        matrixCanvas.preTranslate(-centerX.toFloat(), -centerY.toFloat())
        matrixCanvas.postTranslate(centerX.toFloat(), centerY.toFloat())

        canvas.save()
        canvas.setMatrix(matrixCanvas)

        super.dispatchDraw(canvas)

        canvas.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                cancelSteadyAnimIfNeed()
                rotateCanvasWhenMove(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                rotateCanvasWhenMove(x, y)
                return true
            }
            MotionEvent.ACTION_UP -> {
                startNewSteadyAnim()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun rotateCanvasWhenMove(x: Float, y: Float) {
        val dx = x - centerX
        val dy = y - centerY
        var percentX = dx / (width / 2f)
        var percentY = dy / (height / 2f)
        if (percentX > 1f) {
            percentX = 1f
        } else if (percentX < -1f) {
            percentX = -1f
        }
        if (percentY > 1f) {
            percentY = 1f
        } else if (percentY < -1f) {
            percentY = -1f
        }

        rotateY = MAX_ROTATE_DEGREE * percentX
        rotateX = -(MAX_ROTATE_DEGREE * percentY)

        postInvalidate()
    }

    private fun startNewSteadyAnim() {
        val propertyNameRotateX = "rotateX"
        val propertyNameRotateY = "rotateY"

        val holderRotateX = PropertyValuesHolder.ofFloat(propertyNameRotateX, rotateX, 0f)
        val holderRotateY = PropertyValuesHolder.ofFloat(propertyNameRotateY, rotateY, 0f)

        steadyAnimator = ValueAnimator.ofPropertyValuesHolder(holderRotateX, holderRotateY).apply {
            duration = 1000
            interpolator = BounceInterpolator()
            addUpdateListener { animation: ValueAnimator ->
                rotateX = animation.getAnimatedValue(propertyNameRotateX) as Float
                rotateY = animation.getAnimatedValue(propertyNameRotateY) as Float
                postInvalidate()
            }
        }
        steadyAnimator?.start()
    }

    private fun cancelSteadyAnimIfNeed() {
        if (steadyAnimator?.isStarted == true || steadyAnimator?.isRunning == true) {
            steadyAnimator?.cancel()
        }
    }

    companion object {
        private const val MAX_ROTATE_DEGREE = 20f
    }
}