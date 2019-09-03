package com.sun.heartrate.ui.heartbeat

import android.hardware.camera2.CameraManager

interface HeartbeatContract {
    interface View {
        fun getCameraManager(): CameraManager
        fun closeCamera()
    }
    
    interface Presenter {
        fun openCamera()
        fun closeCamera()
        fun checkCamera(): Boolean
    }
}
