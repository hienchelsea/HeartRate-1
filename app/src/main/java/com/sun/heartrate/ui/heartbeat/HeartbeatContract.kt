package com.sun.heartrate.ui.heartbeat

interface HeartbeatContract {
    interface View {
        fun closeCamera()
    }
    
    interface Presenter {
        fun calculateHeartRate(currentRolling: Int, timeOpen: Long)
    }
}
