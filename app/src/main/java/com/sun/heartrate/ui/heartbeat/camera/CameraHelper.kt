package com.sun.heartrate.ui.heartbeat.camera

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import com.sun.heartrate.ui.heartbeat.callback.OnImageAvailableListener
import com.sun.heartrate.ui.heartbeat.callback.SessionStateCallback
import com.sun.heartrate.ui.heartbeat.callback.StateCallback

class CameraHelper(
    private val manager: CameraManager,
    private val onDataLoadImageCallback: OnDataLoadImageCallback
) {
    private val imageReader: ImageReader by lazy {
        ImageReader.newInstance(
            WIDTH_IMAGE_READER,
            HEIGHT_IMAGE_READER,
            ImageFormat.YUV_420_888,
            MAXIMUM_NUMBER_OF_IMAGE
        )
    }
    private val backgroundThread: HandlerThread by lazy {
        HandlerThread("Camera Background").apply {
            start()
        }
    }
    private val backgroundHandler: Handler by lazy {
        Handler(backgroundThread.looper)
    }
    
    private lateinit var cameraDevice: CameraDevice
    private var timeStart = 0L
    private var isCheckCamera: Boolean = false
    
    @SuppressLint("MissingPermission")
    fun openCamera() {
        try {
            val cameraId = manager.cameraIdList[0]
            manager.run {
                openCamera(cameraId,
                    StateCallback { camera: CameraDevice? ->
                        camera?.let { createCameraPreview(it) }
                    },
                    null
                )
                timeStart = System.currentTimeMillis()
                isCheckCamera = true
            }
            
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        
        
    }
    
    fun closeCamera() {
        cameraDevice.close()
        isCheckCamera = false
        backgroundThread.quitSafely()
    }
    
    private fun createCameraPreview(cameraDevice: CameraDevice) {
        try {
            this.cameraDevice = cameraDevice
            val surfaceArrayList = mutableListOf<Surface>().apply {
                add(imageReader.surface)
            }
            imageReader.setOnImageAvailableListener(
                OnImageAvailableListener { values: ByteArray,
                                           widthImage: Int,
                                           heightImage: Int ->
                    onDataLoadImageCallback.loadDataImage(
                        values,
                        widthImage,
                        heightImage,
                        timeStart
                    )
                },
                null
            
            )
            cameraDevice.createCaptureSession(
                surfaceArrayList,
                SessionStateCallback(cameraDevice, imageReader)
                { cameraCaptureSession: CameraCaptureSession,
                  builder: CaptureRequest.Builder ->
                    updateCameraPreview(cameraCaptureSession, builder)
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        
    }
    
    private fun updateCameraPreview(
        cameraCaptureSessions: CameraCaptureSession,
        captureRequestBuilder: CaptureRequest.Builder
    ) {
        try {
            cameraCaptureSessions.setRepeatingRequest(
                captureRequestBuilder.build(),
                null,
                backgroundHandler
            )
            
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        
    }
    
    fun checkCamera(): Boolean = isCheckCamera
    
    companion object {
        const val WIDTH_IMAGE_READER = 960
        const val HEIGHT_IMAGE_READER = 540
        const val MAXIMUM_NUMBER_OF_IMAGE = 3
    }
    
    interface OnDataLoadImageCallback {
        fun loadDataImage(
            value: ByteArray,
            widthImage: Int,
            heightImage: Int,
            timeStart: Long
        )
    }
    
}
