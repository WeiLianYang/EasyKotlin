package com.william.easykt.test

import com.william.base_component.utils.logD
import com.william.base_component.utils.logE
import com.william.base_component.utils.logW
import com.william.easykt.data.Article
import com.william.easykt.service.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 *  author : WilliamYang
 *  date : 2021/8/17 14:42
 *  description :
 */
class TestRepository {

    suspend fun getData(): MutableList<Article> {
        return Api.apiService.getTopArticles().data
    }

    fun getDataFlow(): Flow<MutableList<Article>> {
        return flow {
            "flow emit value".logD()
            emit(getData())
        }.flowOn(Dispatchers.Default)
            .onCompletion { cause -> "flow completed with $cause".logW() } // 2 观察上下游异常
            .catch { ex ->
                // 3 需要放在 onCompletion 之后，否则发生异常后 onCompletion 不执行
                "flow caught $ex".logE()
                error(ex)
            }
    }

}