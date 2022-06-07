package com.william.easykt


import androidx.test.ext.junit.runners.AndroidJUnit4
import com.blankj.utilcode.util.DeviceUtils
import com.william.easykt.utils.isDeviceRooted
import com.william.easykt.utils.isDeviceRooted2
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 *  author : WilliamYang
 *  date : 2022/6/7 10:31
 *  description : device test
 */
@RunWith(AndroidJUnit4::class)
class DeviceTest {

    @Test
    fun checkDevice_NotRooted1() {
        val flag = isDeviceRooted()
        Assert.assertFalse("This device has been rooted 111 !!!", flag)
    }

    @Test
    fun checkDevice_NotRooted2() {
        val flag = isDeviceRooted2()
        Assert.assertFalse("This device has been rooted 222 !!!", flag)
    }

    @Test
    fun checkDevice_NotRooted3() {
        val flag = DeviceUtils.isDeviceRooted()
        Assert.assertFalse("This device has been rooted 333 !!!", flag)
    }

    @Test
    fun checkDevice_IsNotEmulator() {
        val flag = DeviceUtils.isEmulator()
        Assert.assertFalse("This device is an emulator !!!", flag)
    }

}