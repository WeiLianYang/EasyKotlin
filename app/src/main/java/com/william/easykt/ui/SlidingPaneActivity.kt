package com.william.easykt.ui

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.dp
import com.william.easykt.R
import com.william.easykt.databinding.ActivitySlidingPaneBinding
import com.william.easykt.ui.adapter.PaneMenuAdapter
import com.william.easykt.viewmodel.PaneViewModel
import com.zyyoona7.itemdecoration.RecyclerViewDivider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *  author : WilliamYang
 *  date : 2023/6/12 16:32
 *  description :
 */
@AndroidEntryPoint
class SlidingPaneActivity : BaseActivity() {

    override val viewBinding: ActivitySlidingPaneBinding by bindingView()

    private val viewModel by viewModels<PaneViewModel>()

    @Inject
    lateinit var mAdapter: PaneMenuAdapter

    override fun initData() {
        setTitleText(R.string.test_sliding_pane)

        viewBinding.paneList.apply {
            adapter = mAdapter
            RecyclerViewDivider.linear()
                .asSpace().dividerSize(1.dp)
                .build().addTo(this)
        }

        viewModel.dataList.observe(this) {
            mAdapter.setList(it)
        }

        mAdapter.setOnItemClickListener { _, position, _ ->
            openDetails(position)
        }

        onBackPressedDispatcher.addCallback(
            this, TwoPaneOnBackPressedCallback(viewBinding.slidingPaneLayout)
        )
    }

    private fun openDetails(itemId: Int) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.detail_container, PaneDetailFragment::class.java,
                bundleOf("detail" to "detail: $itemId")
            )
            // If we're already open and the detail pane is visible,
            // crossfade between the fragments.
            if (viewBinding.slidingPaneLayout.isOpen) {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        viewBinding.slidingPaneLayout.open()
    }

    /** 与系统返回按钮集成 **/
    class TwoPaneOnBackPressedCallback(
        private val slidingPaneLayout: SlidingPaneLayout
    ) : OnBackPressedCallback(
        // Set the default 'enabled' state to true only if it is slidable (i.e., the panes
        // are overlapping) and open (i.e., the detail pane is visible).
        slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen
    ), SlidingPaneLayout.PanelSlideListener {

        init {
            slidingPaneLayout.addPanelSlideListener(this)
        }

        override fun handleOnBackPressed() {
            // Return to the list pane when the system back button is pressed.
            slidingPaneLayout.closePane()
        }

        override fun onPanelSlide(panel: View, slideOffset: Float) {}

        override fun onPanelOpened(panel: View) {
            // Intercept the system back button when the detail pane becomes visible.
            isEnabled = true
        }

        override fun onPanelClosed(panel: View) {
            // Disable intercepting the system back button when the user returns to the
            // list pane.
            isEnabled = false
        }
    }

}
