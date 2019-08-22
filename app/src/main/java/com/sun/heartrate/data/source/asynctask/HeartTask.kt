package com.sun.heartrate.data.source.asynctask

import android.os.AsyncTask
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.model.OnDataLoadedCallback

abstract class HeartTask(
    private val callback: OnDataLoadedCallback<List<HeartModel>>
) : AsyncTask<Any?, Any?, List<HeartModel>>() {
    
    private var exception: Exception? = null
    
    abstract fun getData(): List<HeartModel>
    
    override fun doInBackground(vararg p0: Any?): List<HeartModel>? =
        try {
            getData()
        } catch (exception: Exception) {
            this.exception = exception
            null
        }
    
    override fun onPostExecute(result: List<HeartModel>?) {
        result?.let { callback.onDataLoaded(it) }
            ?: exception?.let { callback.onDataNotAvailable(it) }
    }
}
