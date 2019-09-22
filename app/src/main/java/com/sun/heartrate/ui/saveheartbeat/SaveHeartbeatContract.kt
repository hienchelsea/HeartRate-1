package com.sun.heartrate.ui.saveheartbeat

import com.sun.heartrate.data.model.HeartModel

interface SaveHeartbeatContract {
    interface View {
        fun showToastNotification(value: Boolean)
    }
    
    interface Presenter {
        fun saveHeartbeat(heartModel: HeartModel)
    }
}
