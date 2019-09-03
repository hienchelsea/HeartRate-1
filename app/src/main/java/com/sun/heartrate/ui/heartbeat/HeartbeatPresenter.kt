package com.sun.heartrate.ui.heartbeat

import com.sun.heartrate.ui.heartbeat.camera.CameraHelper
import com.sun.heartrate.utils.HandlingTheResult
import com.sun.heartrate.utils.ImageProcessingHelper

class HeartbeatPresenter(
    private val view: HeartbeatContract.View
) : HeartbeatContract.Presenter, CameraHelper.OnDataLoadImageCallback {
    override fun loadDataImage(
        value: ByteArray,
        widthImage: Int,
        heightImage: Int,
        timeStart: Long
    ) {
        val currentRolling = ImageProcessingHelper.imageConversionProcessing(
            value,
            widthImage,
            heightImage
        )
        if (System.currentTimeMillis() - timeStart > MEASUREMENT_TIME) {
            view.closeCamera()
        } else {
            when {
                MIN_ROLLING > currentRolling -> {
                }
                else -> HandlingTheResult.handleResultImage(currentRolling)
            }
        }
    }
    
    private val cameraHelper = CameraHelper(view.getCameraManager(), this)
    
    override fun openCamera() {
        cameraHelper.openCamera()
    }
    
    override fun closeCamera() {
        cameraHelper.closeCamera()
    }
    
    override fun checkCamera(): Boolean = cameraHelper.checkCamera()
    
    
    companion object {
        const val SIZE_AVERAGE_INDEX = 10
        const val MIN_ROLLING = 180
        const val MEASUREMENT_TIME = 26000
    }
}
