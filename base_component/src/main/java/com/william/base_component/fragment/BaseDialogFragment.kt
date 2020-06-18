package com.william.base_component.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

/**
 * @author William
 * @date 2020/5/8 12:25
 * Class Comment：DialogFragment
 */
abstract class BaseDialogFragment : DialogFragment() {

    protected var mFragment: Fragment? = null
    protected var mActivity: FragmentActivity? = null

    protected abstract val mViewBinding: ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = mViewBinding.root
        mFragment = this
        mActivity = activity
        initView(view)
        initAction()
        initData()
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            attributes.apply {
                width = if (getWidth() != 0) getWidth() else WindowManager.LayoutParams.WRAP_CONTENT
                height =
                    if (getHeight() != 0) getHeight() else WindowManager.LayoutParams.WRAP_CONTENT
                gravity = getGravity()
            }
            setBackgroundDrawable(ColorDrawable(0x00000000))
        }
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initView(view: View)

    protected abstract fun initAction()

    protected abstract fun initData()

    protected open fun getWidth() = WindowManager.LayoutParams.WRAP_CONTENT

    protected open fun getHeight() = WindowManager.LayoutParams.WRAP_CONTENT

    protected open fun getTopMargin() = 0

    protected open fun getGravity() = Gravity.CENTER

    protected inline fun <reified T : ViewBinding> bindingView(): Lazy<T> =
        lazy(LazyThreadSafetyMode.NONE) {
            val viewBindClass = T::class.java
            val method = viewBindClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
            return@lazy method.invoke(null, layoutInflater) as T
        }

}