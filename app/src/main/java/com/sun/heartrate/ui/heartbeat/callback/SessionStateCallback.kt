package com.sun.heartrate.ui.heartbeat.callback

import android.annotation.TargetApi
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.os.Build

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class SessionStateCallback(
    private val cameraDevice: CameraDevice,
    private val imageReader: ImageReader,
    private val onUpdatePreview: (session: CameraCaptureSession, capture: CaptureRequest.Builder) -> Unit
) : CameraCaptureSession.StateCallback() {
    override fun onConfigureFailed(session: CameraCaptureSession) {
    }
    
    override fun onConfigured(session: CameraCaptureSession) {
        val captureRequestBuilder: CaptureRequest.Builder = cameraDevice
            .createCaptureRequest(CameraDevice
                .TEMPLATE_PREVIEW).apply {
                addTarget(imageReader.surface)
                set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH)
            }
        onUpdatePreview(session, captureRequestBuilder)
    }
}
