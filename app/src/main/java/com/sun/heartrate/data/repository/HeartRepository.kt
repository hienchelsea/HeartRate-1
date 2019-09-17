package com.sun.heartrate.data.repository

import com.sun.heartrate.data.database.SharedPreferencesUtils
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.model.OnDataLoadedCallback
import com.sun.heartrate.data.source.HeartDataSource
import com.sun.heartrate.data.source.HeartLocalDataSource

class HeartRepository(
    private val heartLocalDataSource: HeartLocalDataSource
) : HeartDataSource {
    
    override fun insetHeart(
        heartModel: HeartModel,
        onDataLoadedCallback: OnDataLoadedCallback<Boolean>
    ) {
        heartLocalDataSource.insetHeart(heartModel, onDataLoadedCallback)
    }
    
    override fun deleteHeart(
        heartModel: HeartModel,
        onDataLoadedCallback: OnDataLoadedCallback<Boolean>
    ) {
        heartLocalDataSource.deleteHeart(heartModel, onDataLoadedCallback)
    }
    
    override fun getAllHearts(onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>) {
        heartLocalDataSource.getAllHearts(onDataLoadedCallback)
    }
    
    override fun getHeartsByMonth(
        month: String,
        onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>
    ) {
        heartLocalDataSource.getHeartsByMonth(month, onDataLoadedCallback)
    }
    
    override fun getHeartsByStatus(
        image: Int,
        onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>
    ) {
        heartLocalDataSource.getHeartsByStatus(image, onDataLoadedCallback)
    }
    
    override fun setStringLanguage(
        sharedPreferencesUtils: SharedPreferencesUtils,
        key: String,
        value: String
    ) {
       heartLocalDataSource.setStringLanguage(sharedPreferencesUtils,key,value)
    }
    
    override fun getStringLanguage(
        sharedPreferencesUtils: SharedPreferencesUtils,
        key: String
    ): String = heartLocalDataSource.getStringLanguage(sharedPreferencesUtils,key)
}
