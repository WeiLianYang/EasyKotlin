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

package com.william.easykt.ui

import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.easykt.R
import com.william.easykt.databinding.ActivityAesEncryptBinding
import com.william.easykt.utils.AesManager


/**
 * @author William
 * @date 2022/3/27 11:15
 * Class Comment：aes demo
 */
class AesEncryptActivity : BaseActivity() {

    override val viewBinding: ActivityAesEncryptBinding by bindingView()

    override fun initView() {
        viewBinding.btnEncrypt.setOnClickListener { encrypt() }

        viewBinding.btnDecrypt.setOnClickListener { decrypt() }
    }

    override fun initAction() {
    }

    override fun initData() {
        setTitleText("AesEncryptActivity")
    }

    private fun encrypt() {
        viewBinding.etPlain.error = null
        viewBinding.etCipher.error = null
        viewBinding.etCipher.setText("")
        try {
            val plaintext = viewBinding.etPlain.text.toString().trim()
            val associatedData = viewBinding.etAssociated.text.toString().trim()
            val ciphertext = AesManager.attemptEncrypt(plaintext, associatedData)
            viewBinding.etCipher.setText(ciphertext)
        } catch (e: Exception) {
            viewBinding.etCipher.error = String.format(
                "%s: %s", getString(R.string.error_cannot_encrypt), e.toString()
            )
            viewBinding.etPlain.requestFocus()
        }
    }

    private fun decrypt() {
        viewBinding.etPlain.error = null
        viewBinding.etPlain.setText("")
        viewBinding.etCipher.error = null
        try {
            val ciphertext = viewBinding.etCipher.text.toString().trim()
            val associatedData = viewBinding.etAssociated.text.toString().trim()
            val plaintext = AesManager.attemptDecrypt(ciphertext, associatedData)
            viewBinding.etPlain.setText(plaintext)
        } catch (e: Exception) {
            viewBinding.etPlain.error = String.format(
                "%s: %s", getString(R.string.error_cannot_decrypt), e.toString()
            )
            viewBinding.etCipher.requestFocus()
        }
    }

}