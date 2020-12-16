package com.william.easykt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.william.base_component.utils.openActivity
import com.william.easykt.test.TestActivity
import com.william.easykt.ui.SwipeCardActivity
import com.william.easykt.ui.WaveAnimationActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_button1.setOnClickListener {
            openActivity(this, TestActivity::class.java)
        }

        tv_button2.setOnClickListener {
            openActivity(this, SwipeCardActivity::class.java)
        }

        tv_button3.setOnClickListener {
            openActivity(this, WaveAnimationActivity::class.java)
        }
    }

}