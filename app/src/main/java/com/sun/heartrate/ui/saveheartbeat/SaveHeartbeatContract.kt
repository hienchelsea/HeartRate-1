package com.sun.heartrate.ui.saveheartbeat

interface SaveHeartbeatContract {
    interface View{
        fun showToastNotification(value:Boolean)
    }
    interface Presenter{
       fun saveHeartbeat()
    }
    
}
