package com.sun.heartrate.data.source.asynctask

import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.model.OnDataLoadedCallback

class StatusHeartAsyncTask(
    private val value: Int,
    private val heartDatabase: HeartDatabase,
    callback: OnDataLoadedCallback<List<HeartModel>>
) : HeartTask<List<HeartModel>>(callback) {
    
    override fun getData() = heartDatabase.getHeartByStatus(value)
}
