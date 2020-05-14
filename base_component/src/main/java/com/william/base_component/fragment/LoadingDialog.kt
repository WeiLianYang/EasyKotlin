package com.william.base_component.fragment

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.william.base_component.BaseApp
import com.william.base_component.R
import com.william.base_component.utils.getIntPixel


/**
 * @author William
 * @date 2020/4/16 16:53
 * Class Comment：加载弹窗
 */
class LoadingDialog : DialogFragment() {

    var canNotCancel = false

    override fun onStart() {
        super.onStart()
        setStyle(STYLE_NORMAL, 0)

        val window: Window? = dialog?.window
        if (window != null) {
            window.setDimAmount(0f)
            val params = window.attributes
            params.apply {
                width = getIntPixel(100)
                height = getIntPixel(100)
                gravity = Gravity.CENTER
            }
            window.attributes = params
            window.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    BaseApp.instance,
                    R.drawable.base_icon_loading_bg
                )
            )
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK && canNotCancel
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.base_dialog_loading, container)
    }

    companion object {

        @JvmStatic
        fun newInstance(): LoadingDialog {
            val bundle = Bundle()
            val fragment = LoadingDialog()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
        }
    }
}
