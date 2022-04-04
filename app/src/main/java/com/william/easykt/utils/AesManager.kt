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

package com.william.easykt.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.william.base_component.extension.logD
import com.william.base_component.extension.logE
import com.william.base_component.extension.logI
import com.william.base_component.extension.logV
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object AesManager {

    private const val TAG = "AesManager"

    /** 密钥别名 **/
    private const val alias = "ek_key_sets"

    /** 加密模式 */
    private const val transformation = "AES/GCM/NoPadding"

    /** 密钥提供者 */
    private const val provider = "AndroidKeyStore"

    /** key 位数 */
    private const val KEY_SIZE_IN_BYTES = 256

    /** 初始化矢量 iv 位数 */
    private const val IV_SIZE_IN_BYTES = 12

    /** 标签位数 */
    private const val TAG_SIZE_IN_BYTES = 16

    /** 对称密钥必须不可预测并且保密 */
    private val key by lazy {
        getKeyFromAndroidKeyStore()
//        generateSecretKey()
    }

    /** 生成key */
    @JvmStatic
    fun generateSecretKey(keySize: Int = KEY_SIZE_IN_BYTES): SecretKey {
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(keySize)
        return keygen.generateKey()
    }

    /**
     * 从系统中获取key
     */
    @JvmStatic
    fun getKeyFromAndroidKeyStore(): SecretKey {
        val keyStore = KeyStore.getInstance(provider)
        keyStore.load(null)
        val keys = keyStore.getKey(alias, null)
        return if (keys == null) {
            "key is null, create new key".logV(TAG)
            // 生成密钥（会自动保存在keyStore中）
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, provider)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
            // 生成key
            keyGenerator.generateKey()
        } else {
            "key is not null".logD(TAG)
            keys as SecretKey
        }
    }

    /**
     * 加密
     * @param plainData 原明文
     * @param associatedData 关联数据
     * @return 密文
     */
    @JvmStatic
    fun encrypt(plainData: String, associatedData: String? = null): String {
        kotlin.runCatching {
            attemptEncrypt(plainData, associatedData)
        }.onSuccess {
            return it
        }.onFailure {
            "decrypt: onFailure, $it".logE(TAG)
        }
        return ""
    }

    /**
     * 加密
     * @param plainData 原明文
     * @param associatedData 关联数据
     * @return 密文
     */
    @JvmStatic
    @Throws(
        GeneralSecurityException::class,
        IllegalArgumentException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class
    )
    fun attemptEncrypt(plainData: String, associatedData: String? = null): String {
        // 1. 获取 Cipher，并初始化
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.ENCRYPT_MODE, key)

        // 2. 获取 初始化矢量
        val iv = cipher.iv

        // 3. 使用关联数据，继续对附加身份验证数据 (AAD) 进行多部分更新。
        val associatedBytes = associatedData?.toByteArray()
        if (associatedBytes != null && associatedBytes.isNotEmpty()) {
            cipher.updateAAD(associatedBytes)
        }

        // 4. 加密数据
        val cipherBytes = cipher.doFinal(plainData.toByteArray())

        // 5. 将 iv 和 密文 组合保存
        val buffer = ByteBuffer.allocate(iv.size + cipherBytes.size)
        buffer.put(iv)
        buffer.put(cipherBytes)

        // 6. 获取组合后的 base64 文本
        val result = bytesToBase64(buffer.array())


        "encrypt, key info: ${key.javaClass.simpleName}, algorithm: ${key.algorithm}, format: ${key.format}, " +
                "base64 of Key: ${bytesToBase64(key.encoded)}, plainData: $plainData, associatedData: $associatedData, base64 of IV: ${
                    bytesToBase64(
                        iv
                    )
                }, result: $result".logV(TAG)

        return result
    }

    /**
     * 解密
     * @param cipherData 密文
     * @param associatedData 关联数据
     * @return 原明文
     */
    @JvmStatic
    fun decrypt(cipherData: String, associatedData: String? = null): String {
        kotlin.runCatching {
            attemptDecrypt(cipherData, associatedData)
        }.onSuccess {
            return it
        }.onFailure {
            "decrypt: onFailure, $it".logE(TAG)
        }
        return ""
    }

    /**
     * 解密
     * @param cipherData 密文
     * @param associatedData 关联数据
     * @return 原明文
     */
    @JvmStatic
    @Throws(
        GeneralSecurityException::class,
        IllegalArgumentException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class
    )
    fun attemptDecrypt(cipherData: String, associatedData: String? = null): String {
        // 1. 将密文转为字节数组，分别提取 iv 和 密文
        val mixedBytes = base64ToBytes(cipherData)
        val buffer = ByteBuffer.wrap(mixedBytes)
        // 获取 iv。初始化矢量必须唯一且不可预测，但不必保密。
        val iv = ByteArray(IV_SIZE_IN_BYTES)
        buffer.get(iv)
        // 获取 密文
        val cipherBytes = ByteArray(buffer.remaining())
        buffer.get(cipherBytes)

        // 2. 获取 Cipher，并初始化
        val cipher = Cipher.getInstance(transformation)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_SIZE_IN_BYTES * 8, iv))

        // 3. 使用关联数据，继续对附加身份验证数据 (AAD) 进行多部分更新。
        val associatedBytes = associatedData?.toByteArray()
        if (associatedBytes != null && associatedBytes.isNotEmpty()) {
            cipher.updateAAD(associatedBytes)
        }

        // 4. 解密密文，转为 String 文本
        val result = String(cipher.doFinal(cipherBytes))


        "decrypt, key info: ${key.javaClass.simpleName}, algorithm: ${key.algorithm}, format: ${key.format}, base64 of Key: ${
            bytesToBase64(
                key.encoded
            )
        }, cipherData: $cipherData, associatedData: $associatedData, base64 of IV: ${
            bytesToBase64(
                iv
            )
        }, result: $result".logI(TAG)

        return result
    }

}
