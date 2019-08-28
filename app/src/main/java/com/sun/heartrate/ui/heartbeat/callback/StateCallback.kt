package com.sun.heartrate.ui.heartbeat.callback

import android.annotation.TargetApi
import android.hardware.camera2.CameraDevice
import android.os.Build
import com.sun.heartrate.ui.heartbeat.HeartbeatFragment

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class StateCallback(
    private val cameraPreview: (camera: CameraDevice?) -> Unit
) : CameraDevice.StateCallback() {
    
    private var cameraDevice: CameraDevice? = null
    override fun onOpened(camera: CameraDevice) {
        cameraPreview(camera)
        cameraDevice = camera
    }
    
    override fun onDisconnected(camera: CameraDevice) {
        cameraPreview(cameraDevice)
    }
    
    override fun onError(camera: CameraDevice, error: Int) {
        cameraPreview(cameraDevice)
    }
    
}
