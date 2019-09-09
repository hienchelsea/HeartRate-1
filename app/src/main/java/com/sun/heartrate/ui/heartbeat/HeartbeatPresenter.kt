package com.sun.heartrate.ui.heartbeat

import com.sun.heartrate.R
import com.sun.heartrate.utils.HandlingTheResult
import com.sun.heartrate.utils.ImageProcessingHelper

class HeartbeatPresenter(
    private val view: HeartbeatContract.View
) : HeartbeatContract.Presenter {
    
    override fun getDataImage(
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
        view.run {
            if (System.currentTimeMillis() - timeStart > MEASUREMENT_TIME) {
                closeCamera()
            } else {
                when {
                    MIN_ROLLING > currentRolling -> {
                        displayGuideline(R.string.title_please_put_your_hand_on_the_camera)
                    }
                    else -> {
                        displayGuideline(R.string.title_measuring_heart_rate)
                        calculateTheResultOfHeartRate(currentRolling,timeStart)
                    }
                }
            }
        }
    }
    
    private fun calculateTheResultOfHeartRate(currentRolling: Int,timeStart: Long) {
        val heartAvg = HandlingTheResult.handleResultImage(currentRolling,timeStart)
        if (heartAvg > 0) view.displayHeatRate(heartAvg)
    }
    
    companion object {
        const val SIZE_AVERAGE_INDEX = 10
        const val MIN_ROLLING = 180
        const val MEASUREMENT_TIME = 26000
    }
}
