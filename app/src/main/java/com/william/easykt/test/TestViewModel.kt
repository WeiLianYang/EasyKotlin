package com.william.easykt.test

import androidx.lifecycle.*
import com.william.base_component.extension.launchFlow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch

/**
 *  author : WilliamYang
 *  date : 2021/8/17 14:22
 *  description :
 */
class TestViewModel(private val repository: TestRepository) : ViewModel() {

    val testData = MutableLiveData<String>()

    private val testDataFlow = repository.getDataFlow()

    val testData2 = testDataFlow.asLiveData()

    fun requestData() {
        launchFlow(
            { repository.getData() },
            { testData.value = it.toString() }
        )
    }

    fun requestData2() {
        viewModelScope.launch {
            testDataFlow.single()
        }
    }

    companion object {

        fun provideFactory(repository: TestRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return TestViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}