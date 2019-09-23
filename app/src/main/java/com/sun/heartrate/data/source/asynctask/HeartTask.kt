package com.sun.heartrate.data.source.asynctask

import android.os.AsyncTask
import com.sun.heartrate.data.model.OnDataLoadedCallback

abstract class HeartTask<T>(
    private val callback: OnDataLoadedCallback<T>
) : AsyncTask<Any?, Any?, T>() {
    
    private var exception: Exception? = null
    
    abstract fun getData(): T
    
    override fun doInBackground(vararg p0: Any?): T? =
        try {
            getData()
        } catch (exception: Exception) {
            this.exception = exception
            null
        }
    
    override fun onPostExecute(result: T?) {
        result?.let { callback.onDataLoaded(it) }
            ?: exception?.let { callback.onDataNotAvailable(it) }
    }
}
