package com.william.easykt.utils

import android.os.Build
import com.william.base_component.extension.logD
import com.william.base_component.extension.logE
import com.william.base_component.extension.logI
import com.william.base_component.extension.logW
import java.io.*


/**
 *  author : WilliamYang
 *  date : 2022/6/7 10:54
 *  description : 检查设备是否已 root
 */

/** 检查设备 root 状态 **/
fun isDeviceRooted(): Boolean {
    if (checkBuildTags()) {
        return true
    }
    if (checkSuperuserApk()) {
        return true
    }
    if (checkRootPathSU()) {
        return true
    }
//    if (checkRootWhichSU()) {
//        return true
//    }
    if (checkBusybox()) {
        return true
    }
    if (checkAccessRootData()) {
        return true
    }
    if (checkGetRootAuth()) {
        return true
    }
    return false
}

/** 检查 Build tags。我们可以查看发布的系统版本，是test-keys（测试版），还是release-keys（发布版） **/
fun checkBuildTags(): Boolean {
    val tags = Build.TAGS
    if (tags != null && tags.contains("test-keys")) {
        "checkBuildTags: $tags".logW()
        return true
    }
    return false
}

/**
 * 检查 Superuser.apk 是否存在。
 * Superuser.apk是一个被广泛使用的用来root安卓设备的软件，所以可以检查这个app是否存在。
 */
fun checkSuperuserApk(): Boolean {
    try {
        val file = File("/system/app/Superuser.apk")
        if (file.exists()) {
            "/system/app/Superuser.apk exist".logW()
            return true
        }
    } catch (e: Exception) {
        "checkSuperuserApk: $e".logE()
    }
    return false
}

/**
 * su是Linux下切换用户的命令，在使用时不带参数，就是切换到超级用户。
 * 通常我们获取root权限，就是使用su命令来实现的，所以可以检查这个命令是否存在。
 */
fun checkRootPathSU(): Boolean {
    var file: File?
    val paths = arrayOf("/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/")
    try {
        for (i in paths.indices) {
            file = File(paths[i] + "su")
            if (file.exists()) {
                "find su in : ${paths[i]}".logW()
                return true
            }
        }
    } catch (e: Exception) {
        "checkRootPathSU: $e".logE()
    }
    return false
}

/**
 * 使用which命令查看是否存在 su。which 是 linux 下的一个命令，可以在系统 PATH 变量指定的路径中搜索某个系统命令的位置并且返回第一个搜索结果。
 * 缺陷一：就是需要系统中存在which这个命令。有的Android系统中没有这个命令
 * 缺陷二：就是可能系统中存在su，但是已经失效的情况。例如，曾经root过，后来又取消了
 */
fun checkRootWhichSU(): Boolean {
    val strCmd = arrayOf("/system/xbin/which", "su")
    val execResult = executeCommand(strCmd)
    "execResult: $execResult".logW()
    return execResult != null
}

/** 执行 shell 命令 **/
fun executeCommand(cmd: Array<String>?): ArrayList<String?>? {
    var line: String?
    val fullResponse = ArrayList<String?>()
    val process: Process? = try {
        "to shell exec which for find su :".logD()
        Runtime.getRuntime().exec(cmd)
    } catch (e: Exception) {
        return null
    }
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(InputStreamReader(process?.inputStream))
        while (reader.readLine().also { line = it } != null) {
            "–> Line received: $line".logD()
            fullResponse.add(line)
        }
    } catch (e: Exception) {
        "executeCommand: $e".logE()
    } finally {
        reader?.close()
    }
    "–> Full response was: $fullResponse".logD()
    return fullResponse
}

/**
 * 最靠谱的。执行su，看能否获取到root权限。
 * 缺点：在已经root的设备上，会弹出提示框，请求给app开启root权限。
 */
@Synchronized
fun checkGetRootAuth(): Boolean {
    var process: Process? = null
    var os: DataOutputStream? = null
    return try {
        "to exec su".logD()
        process = Runtime.getRuntime().exec("su")
        os = DataOutputStream(process.outputStream)
        os.writeBytes("exit\n")
        os.flush()
        val exitValue = process.waitFor()
        "exitValue: $exitValue".logD()
        exitValue == 0
    } catch (e: Exception) {
        "checkGetRootAuth catch: $e".logE()
        false
    } finally {
        try {
            os?.close()
            process?.destroy()
        } catch (e: Exception) {
            "checkGetRootAuth finally: $e".logE()
        }
    }
}

/**
 * 简单的说BusyBox就好像是个大工具箱，它集成压缩了 Linux 的许多工具和命令。
 * 所以若设备root了，很可能Busybox也被安装上了。
 */
@Synchronized
fun checkBusybox(): Boolean {
    return try {
        "to exec busybox df".logD()
        val cmd = arrayOf("busybox", "df")
        val execResult = executeCommand(cmd)
        "checkBusybox execResult: $execResult".logW()
        execResult != null
    } catch (e: Exception) {
        "checkBusybox: $e".logE()
        false
    }
}

/**
 * 检查在Android系统中，是否能访问系统目录 /data、/system、/etc。
 * 先写入一个文件，然后读出，查看内容是否匹配，若匹配，才认为系统已经root了。
 */
@Synchronized
fun checkAccessRootData(): Boolean {
    return try {
        "to write /data".logD()
        val fileContent = "test_ok"
        val writeFlag = writeFile("/data/su_test", fileContent)
        if (writeFlag) {
            "write ok".logI()
        } else {
            "write failed".logE()
        }
        "to read /data".logD()
        val content = readFile("/data/su_test")
        "checkAccessRootData content: $content".logD()
        fileContent == content
    } catch (e: Exception) {
        "checkAccessRootData: $e".logE()
        false
    }
}

/** 写文件 **/
fun writeFile(fileName: String?, message: String): Boolean {
    return try {
        val outputStream = FileOutputStream(fileName)
        val bytes = message.toByteArray()
        outputStream.write(bytes)
        outputStream.close()
        true
    } catch (e: Exception) {
        "writeFile: $e".logE()
        false
    }
}

/** 读文件 **/
fun readFile(fileName: String): String? {
    val file = File(fileName)
    var fis: FileInputStream? = null
    var bos: ByteArrayOutputStream? = null
    return try {
        fis = FileInputStream(file)
        bos = ByteArrayOutputStream()
        val bytes = ByteArray(1024)
        var len: Int
        while (fis.read(bytes).also { len = it } > 0) {
            bos.write(bytes, 0, len)
        }
        val result = String(bos.toByteArray())
        result.logD()
        result
    } catch (e: Exception) {
        "readFile: $e".logE()
        null
    } finally {
        bos?.close()
        fis?.close()
    }
}

/**
 * 检测是否 root
 */
fun isDeviceRooted2(): Boolean {
    val paths = arrayOf("/system/bin/su", "/system/xbin/su")
    paths.forEach {
        return File(it).exists() && isExecutable(it)
    }
    return false
}

private fun isExecutable(filePath: String): Boolean {
    var process: Process? = null
    var reader: BufferedReader? = null
    try {
        process = Runtime.getRuntime().exec("ls -l $filePath")
        // 获取返回内容
        reader = BufferedReader(InputStreamReader(process.inputStream))
        val str: String? = reader.readLine()
        if (str != null && str.length >= 4) {
            val flag = str[3]
            if (flag == 's' || flag == 'x') return true
        }
    } catch (e: IOException) {
        "isExecutable: $e".logE()
    } finally {
        reader?.close()
        process?.destroy()
    }
    return false
}