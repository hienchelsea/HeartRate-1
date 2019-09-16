package com.sun.heartrate.ui.saveheartbeat

import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.model.OnDataLoadedCallback
import com.sun.heartrate.data.repository.HeartRepository

class SaveHeartbeatPresenter(
    private val heartRepository: HeartRepository,
    private val view: SaveHeartbeatContract.View,
    private val heartModel: HeartModel
) : SaveHeartbeatContract.Presenter ,OnDataLoadedCallback<Boolean>{
    
    override fun saveHeartbeat() {
        heartRepository.insetHeart(heartModel,this)
    }
    
    override fun onDataLoaded(data: Boolean) {
        view.showToastNotification(data)
    }
    
    override fun onDataNotAvailable(exception: Exception) {
        view.showToastNotification(false)
    }
}
