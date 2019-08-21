package com.sun.heartrate.data.source

import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.model.OnDataLoadedCallback

class HeartLocalDataSource(
    private val heartDatabase: HeartDatabase
) : HeartDataSource {
    override fun insetHeart(
        heartModel: HeartModel,
        onDataLoadedCallback: OnDataLoadedCallback<Boolean>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun deleteHeart(
        heartModel: HeartModel,
        onDataLoadedCallback: OnDataLoadedCallback<Boolean>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun getAllHearts(onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun getHeartsByMonth(
        month: String,
        onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun getHeartsByStatus(
        image: Int,
        onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
}
