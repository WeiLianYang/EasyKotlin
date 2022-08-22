package com.william.easykt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.william.easykt.test.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *  author : WilliamYang
 *  date : 2022/8/22 16:31
 *  description :
 */
@HiltViewModel
class GlideViewModel @Inject constructor() : ViewModel() {

    val list = liveData {
        val list = listOf(
            IMAGE_URL1, IMAGE_URL2, IMAGE_URL3, IMAGE_URL4,
            IMAGE_URL5, IMAGE_URL6, IMAGE_URL7, IMAGE_URL8
        )
        emit(list)
    }
}