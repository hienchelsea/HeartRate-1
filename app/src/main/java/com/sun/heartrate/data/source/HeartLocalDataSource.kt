package com.sun.heartrate.data.source

import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.model.OnDataLoadedCallback
import com.sun.heartrate.data.source.asynctask.*

class HeartLocalDataSource(
    private val heartDatabase: HeartDatabase
) : HeartDataSource {
    
    override fun insetHeart(
        heartModel: HeartModel,
        onDataLoadedCallback: OnDataLoadedCallback<Boolean>
    ) {
        SaveHeartAsyncTask(
            heartModel,
            heartDatabase,
            onDataLoadedCallback
        ).execute()
    }
    
    override fun deleteHeart(
        heartModel: HeartModel,
        onDataLoadedCallback: OnDataLoadedCallback<Boolean>
    ) {
        DeleteHeartAsyncTask(
            heartModel,
            heartDatabase,
            onDataLoadedCallback
        ).execute()
    }
    
    override fun getAllHearts(
        onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>
    ) {
        AllHeartAsyncTask(
            heartDatabase,
            onDataLoadedCallback
        ).execute()
    }
    
    override fun getHeartsByMonth(
        month: String,
        onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>
    ) {
        MonthlyHeartAsyncTask(
            month,
            heartDatabase,
            onDataLoadedCallback
        ).execute()
    }
    
    override fun getHeartsByStatus(
        image: Int,
        onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>
    ) {
        StatusHeartAsyncTask(
            image,
            heartDatabase,
            onDataLoadedCallback
        ).execute()
    }
}
