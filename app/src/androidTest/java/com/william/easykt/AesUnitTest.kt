/*
 * Copyright WeiLianYang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.william.easykt

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.william.base_component.extension.logV
import com.william.easykt.utils.AesManager
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


/**
 * @author William
 * @date 2022/4/3 22:15
 * Class Comment：AES unit test
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class AesUnitTest {

    @Test
    fun testAes() {
        val plainText = "123!@#$%^&*()/ABCdef"
        val associatedData = "123qwe"
        val encryptResult = AesManager.encrypt(plainText, associatedData)

        "----------------------------------------------------".logV()

        val decryptResult = AesManager.decrypt(encryptResult, associatedData)

        Assert.assertTrue(plainText == decryptResult)
    }
}