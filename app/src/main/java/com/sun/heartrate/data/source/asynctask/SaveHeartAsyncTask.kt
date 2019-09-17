package com.sun.heartrate.data.source.asynctask

import android.os.AsyncTask
import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.model.OnDataLoadedCallback

class SaveHeartAsyncTask(
    private val heartModel: HeartModel,
    private val heartDatabase: HeartDatabase,
    private val callback: OnDataLoadedCallback<Boolean>
): AsyncTask<Any?, Any?, Boolean>() {
    
    private var exception: Exception? = null
    override fun doInBackground(vararg p0: Any?): Boolean? =
        try {
            heartDatabase.insertHeart(heartModel)
        } catch (exception: Exception) {
            this.exception = exception
            null
        }
    
    override fun onPostExecute(result: Boolean?) {
        result?.let { callback.onDataLoaded(it) }
            ?: exception?.let { callback.onDataNotAvailable(it) }
    }
}
