package com.sun.heartrate.ui.history

import com.sun.heartrate.data.model.HeartModel

interface HistoryContract {
    interface View {
        fun displayListHeartbeat(hearts: List<HeartModel>, heartsMonths: List<String>)
        fun displayListHeartbeatByStatus(hearts: List<HeartModel>)
        fun displayListHeartbeatByMonth(month: String, hearts: List<HeartModel>)
        fun confirmDeleted(successful: Boolean, heartModel: HeartModel, check: Int)
        fun onError(exception: String)
    }
    
    interface Presenter {
        fun getAllHeartbeat()
        fun getHeartbeatByStatus(status: Int)
        fun deleteHeartbeat(heartModel: HeartModel, check: Int)
        fun getHeartbeatByMonth(month: String)
    }
}
