package com.sun.heartrate.data.source.asynctask

import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.model.OnDataLoadedCallback

class DeleteHeartAsyncTask(
    private val heartModel: HeartModel,
    private val heartDatabase: HeartDatabase,
    callback: OnDataLoadedCallback<Boolean>
) : HeartTask<Boolean>(callback) {
    
    override fun getData(): Boolean = heartDatabase.deleteHeart(heartModel)
}
