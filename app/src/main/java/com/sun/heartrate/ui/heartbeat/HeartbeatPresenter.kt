package com.sun.heartrate.ui.heartbeat

import com.sun.heartrate.data.repository.HeartRepository
import com.sun.heartrate.utils.HandlingTheResult

class HeartbeatPresenter(
    private val heartbeatFragment: HeartbeatFragment,
    private val heartRepository: HeartRepository
) : HeartbeatContract.Presenter {
    
    override fun calculateHeartRate(currentRolling: Int, timeOpen: Long) {
        if (System.currentTimeMillis() - timeOpen > MEASUREMENT_TIME) {
            heartbeatFragment.closeCamera()
        } else {
            when {
                MIN_ROLLING > currentRolling -> {
                }
                else -> HandlingTheResult.handleResultImage(currentRolling)
            }
        }
        
    }
    
    companion object {
        const val SIZE_AVERAGE_INDEX = 10
        const val MIN_ROLLING = 180
        const val MEASUREMENT_TIME = 26000
    }
}
