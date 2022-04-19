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

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * RSA 加解密 帮助类
 */
object RsaHelper {

    /** 密钥长度 */
    const val KEY_SIZE = 2048

    /** 加密算法 */
    private const val ALGORITHM = "RSA"

    /** 转换模式 */
    const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"

    /** 字符集 */
    private const val DEFAULT_CHARSET = "UTF-8"

    var publicKey = ""
    var privateKey = ""

    /**
     * 用公钥加密
     *
     * @param data           要加密的数据
     * @param key            公钥
     * @param keySize        密钥长度
     * @param transformation 转换名称
     * @return 密文
     */
    @JvmOverloads
    fun encrypt(
        data: String,
        key: String? = publicKey,
        keySize: Int = KEY_SIZE,
        transformation: String? = TRANSFORMATION
    ): String? {
        var out: ByteArrayOutputStream? = null
        return try {
            val keyBytes = Base64.decode(key, Base64.DEFAULT)
            val x509KeySpec = X509EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance(ALGORITHM)
            val publicK: Key = keyFactory.generatePublic(x509KeySpec)
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.ENCRYPT_MODE, publicK)
            val dataBytes = data.toByteArray(charset(DEFAULT_CHARSET))
            val inputLength = dataBytes.size

            // 分段加密长度
            val maxLength = keySize / 8 - 11
            out = ByteArrayOutputStream()
            var offSet = 0
            var cache: ByteArray
            var i = 0
            // 分段加密
            while (inputLength - offSet > 0) {
                cache = if (inputLength - offSet > maxLength) {
                    cipher.doFinal(dataBytes, offSet, maxLength)
                } else {
                    cipher.doFinal(dataBytes, offSet, inputLength - offSet)
                }
                out.write(cache, 0, cache.size)
                i++
                offSet = i * maxLength
            }
            val encryptedData = out.toByteArray()
            Base64.encodeToString(encryptedData, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 用私钥解密
     *
     * @param data           要解密的数据
     * @param key            私钥
     * @param keySize        密钥长度
     * @param transformation 转换名称
     * @return 原文
     */
    fun decrypt(data: String?, key: String?, keySize: Int, transformation: String?): String? {
        var out: ByteArrayOutputStream? = null
        return try {
            val dataBytes = Base64.decode(data, Base64.DEFAULT)
            val keyBytes = Base64.decode(key, Base64.DEFAULT)
            val pkcs8KeySpec = PKCS8EncodedKeySpec(keyBytes)
            val keyFactory = KeyFactory.getInstance(ALGORITHM)
            val privateK: Key = keyFactory.generatePrivate(pkcs8KeySpec)
            val cipher = Cipher.getInstance(transformation)
            cipher.init(Cipher.DECRYPT_MODE, privateK)
            val inputLen = dataBytes.size

            // 分段解密长度
            val maxLength = keySize / 8
            out = ByteArrayOutputStream()
            var offSet = 0
            var cache: ByteArray
            var i = 0
            // 分段解密
            while (inputLen - offSet > 0) {
                cache = if (inputLen - offSet > maxLength) {
                    cipher.doFinal(dataBytes, offSet, maxLength)
                } else {
                    cipher.doFinal(dataBytes, offSet, inputLen - offSet)
                }
                out.write(cache, 0, cache.size)
                i++
                offSet = i * maxLength
            }
            out.toString(DEFAULT_CHARSET)
        } catch (e: Exception) {
            null
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param keySize 密钥长度，范围：512～2048, 一般1024
     * @return KeyPair
     */
    fun generateRSAKeyPair(keySize: Int = KEY_SIZE): KeyPair? {
        return try {
            val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM)
            kpg.initialize(keySize)
            kpg.genKeyPair()
        } catch (e: Exception) {
            null
        }
    }

}