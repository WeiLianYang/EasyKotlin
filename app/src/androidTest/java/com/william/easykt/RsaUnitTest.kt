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
import com.william.base_component.utils.logD
import com.william.base_component.utils.logI
import com.william.base_component.utils.logV
import com.william.easykt.utils.RsaHelper
import com.william.easykt.utils.bytesToBase64
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


/**
 * @author William
 * @date 2022/4/3 21:52
 * Class Comment：RSA unit test
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class RsaUnitTest {

    @Test
    fun testRsa() {

        val keyPair = RsaHelper.generateRSAKeyPair()

        RsaHelper.publicKey = bytesToBase64(keyPair?.public?.encoded)
        RsaHelper.privateKey = bytesToBase64(keyPair?.private?.encoded)

        val plainText = "123!@#$%^&*()/ABCdef"
        val encryptResult = RsaHelper.encrypt(plainText)
        "encrypt result: $encryptResult".logD()

        "----------------------------------------------------".logV()

        val decryptResult = RsaHelper.decrypt(
            encryptResult,
            RsaHelper.privateKey,
            RsaHelper.KEY_SIZE,
            RsaHelper.TRANSFORMATION
        )
        "decrypt result: $decryptResult".logI()

        Assert.assertTrue(plainText == decryptResult)
    }
}