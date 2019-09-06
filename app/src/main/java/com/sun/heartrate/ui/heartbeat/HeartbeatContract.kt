package com.sun.heartrate.ui.heartbeat

interface HeartbeatContract {
    interface View {
        fun closeCamera()
        fun displayHeatRate(rateNumber: Int)
        fun displayGuideline(guideline: Int)
    }
    
    interface Presenter {
        
        fun getDataImage(
            value: ByteArray,
            widthImage: Int,
            heightImage: Int,
            timeStart: Long
        )
    }
}
