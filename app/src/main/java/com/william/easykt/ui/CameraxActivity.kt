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

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.os.Build
import android.provider.MediaStore
import android.view.Surface
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.william.base_component.activity.BaseActivity
import com.william.base_component.extension.bindingView
import com.william.base_component.extension.logD
import com.william.base_component.extension.logE
import com.william.base_component.extension.toast
import com.william.base_component.utils.showSnackbar
import com.william.easykt.R
import com.william.easykt.databinding.ActivityCameraxBinding
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * @author William
 * @date 2022/5/7 22:01
 * Class Comment：camerax demo
 */
class CameraxActivity : BaseActivity() {

    override val viewBinding by bindingView<ActivityCameraxBinding>()

    private var imageCapture: ImageCapture? = null

    private var videoCapture: VideoCapture<Recorder>? = null

    private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService

    private var displayId = -1
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var cameraProvider: ProcessCameraProvider? = null

    private val displayManager by lazy {
        getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    override fun initAction() {
        cameraExecutor = Executors.newSingleThreadExecutor()

        viewBinding.viewFinder.post {
            displayId = viewBinding.viewFinder.display.displayId
        }
        // 每次设备的方向发生变化时，更新相机用例
        displayManager.registerDisplayListener(displayListener, null)

        // Request permissions
        if (allPermissionsGranted()) {
            setUpCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE)
        }

        viewBinding.btnCaptureImage.setOnClickListener { takePhoto() }
        viewBinding.btnCaptureVideo.setOnClickListener { captureVideo() }

        viewBinding.btnSwitchLensFacing.setOnClickListener {
            lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) {
                CameraSelector.LENS_FACING_BACK
            } else {
                CameraSelector.LENS_FACING_FRONT
            }
            // 重新绑定相机用例
            bindCameraUseCases()
        }
    }

    override fun initData() {
        setTitleText(R.string.test_camerax)
    }

    /**
     * 拍照
     */
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val name =
            SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraXAlbum")
            }
        }

        val metadata = ImageCapture.Metadata().apply {
            // 使用前置摄像头时镜像
            isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//            .setMetadata(metadata)
            .build()

        // 拍照后回调事件
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(e: ImageCaptureException) {
                    "Photo capture failed: $e".logE()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    msg.toast()
                    msg.logD()
                }
            }
        )
    }

    /**
     * 录像
     */
    private fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

        viewBinding.btnCaptureVideo.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            // 停止当前录制会话
            curRecording.stop()
            recording = null
            return
        }

        // 创建并开始一个新的录制会话
        val name =
            SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraXVideo")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(
                        this@CameraxActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    // 如果获取了权限，录制中开启音频
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        viewBinding.btnCaptureVideo.apply {
                            text = getString(R.string.stop_capture)
                            isEnabled = true
                        }
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val msg =
                                "Video capture succeeded: ${recordEvent.outputResults.outputUri}"
                            msg.toast()
                            msg.logD()
                        } else {
                            recording?.close()
                            recording = null
                            "Video capture ends with error: ${recordEvent.error}".logE()
                        }
                        viewBinding.btnCaptureVideo.apply {
                            text = getString(R.string.start_capture)
                            isEnabled = true
                        }
                    }
                }
            }
    }

    /**
     * 启动相机
     */
    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // 绑定摄像机的生命周期
            cameraProvider = cameraProviderFuture.get()

            // 镜头朝向
            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * 绑定相机用例
     */
    private fun bindCameraUseCases() {
        val rotation = viewBinding.viewFinder.display?.rotation ?: Surface.ROTATION_0

        val cameraProvider =
            cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        // 预览 相机取景器
        val preview = Preview.Builder()
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
            }

        // 录制音视频
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()

        videoCapture = VideoCapture.withOutput(recorder)

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(rotation)
            .build()

        // 创建图片分析
        /*
        val imageAnalyzer = ImageAnalysis.Builder()
            .setTargetRotation(rotation)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { l ->
                    "Average luminosity: $l".logD()
                })
            }
            */

        // 构建摄像头
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        try {
            // 在重新绑定之前取消绑定用例
            cameraProvider.unbindAll()

            // 将用例绑定到相机
            // Preview + VideoCapture + ImageCapture： LIMITED设备及以上。
            // Preview + VideoCapture + ImageAnalysis：（ LEVEL_3最高）设备添加到 Android 7(N)。
            // 预览 + VideoCapture + ImageAnalysis + ImageCapture：不支持。
            val camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, videoCapture
            )
            observeCameraState(camera.cameraInfo)
        } catch (e: Exception) {
            "Use case binding failed, $e".logE()
        }
    }

    /**
     * 如果设备有可用的后置摄像头，则返回 true
     */
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /**
     * 如果设备有可用的前置摄像头，则返回 true
     */
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    private fun observeCameraState(cameraInfo: CameraInfo) {
        cameraInfo.cameraState.observe(this) { cameraState ->

            when (cameraState.type) {
                CameraState.Type.PENDING_OPEN -> {
                    "CameraState: Pending Open".logD()
                }
                CameraState.Type.OPENING -> {
                    "CameraState: Opening".logD()
                }
                CameraState.Type.OPEN -> {
                    "CameraState: Open".logD()
                }
                CameraState.Type.CLOSING -> {
                    "CameraState: Closing".logD()
                }
                CameraState.Type.CLOSED -> {
                    "CameraState: Closed".logD()
                }
            }

            cameraState.error?.let { error ->
                when (error.code) {
                    CameraState.ERROR_STREAM_CONFIG -> {
                        "Stream config error".logD()
                    }
                    CameraState.ERROR_CAMERA_IN_USE -> {
                        "Camera in use".logD()
                    }
                    CameraState.ERROR_MAX_CAMERAS_IN_USE -> {
                        "Max cameras in use".logD()
                    }
                    CameraState.ERROR_OTHER_RECOVERABLE_ERROR -> {
                        "Other recoverable error".logD()
                    }
                    CameraState.ERROR_CAMERA_DISABLED -> {
                        "Camera disabled".logD()
                    }
                    CameraState.ERROR_CAMERA_FATAL_ERROR -> {
                        "Fatal error".logD()
                    }
                    CameraState.ERROR_DO_NOT_DISTURB_MODE_ENABLED -> {
                        "Do not disturb mode enabled".logD()
                    }
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (allPermissionsGranted()) {
                setUpCamera()
            } else {
                showSnackbar(viewBinding.root, "Permissions not granted by the user.")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        displayManager.unregisterDisplayListener(displayListener)
    }

    companion object {
        private const val REQUEST_CODE = 1
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    /**
     * 在配置更改时膨胀相机控件并手动更新 UI，以避免从视图层次结构中删除和重新添加取景器；
     * 这在支持它的设备上提供了无缝的旋转过渡。注意：从 Android 8 开始支持该标志，但对于运行 Android 9 或更低版本的设备，屏幕上仍有小闪烁。
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        bindCameraUseCases()
    }

    /**
     * 对于不触发配置更改的方向更改，我们需要一个显示侦听器，
     * 例如，如果我们选择覆盖清单中的配置更改或 180 度方向更改。
     */
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) {}
        override fun onDisplayRemoved(displayId: Int) {}

        override fun onDisplayChanged(displayId: Int) = viewBinding.root.let { view ->
            if (displayId == this@CameraxActivity.displayId) {
                "Rotation changed: ${view.display.rotation}".logD()
                imageCapture?.targetRotation = view.display.rotation
            }
        }
    }

    /**
     * 亮度分析
     */
    private class LuminosityAnalyzer(private val listener: (Double) -> Unit) :
        ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // 将缓冲区倒回零
            val data = ByteArray(remaining())
            get(data)   // 将此缓冲区中的字节传输到给定的目标数组中
            return data
        }

        override fun analyze(image: ImageProxy) {
            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val l = pixels.average()

            listener(l)

            image.close()
        }
    }

}