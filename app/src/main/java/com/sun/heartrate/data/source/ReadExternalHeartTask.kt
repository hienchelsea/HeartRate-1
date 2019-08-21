package com.sun.heartrate.data.source

import android.os.AsyncTask
import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.model.OnDataLoadedCallback

class ReadExternalHeartTask<T>(
    private val heartDatabase: HeartDatabase,
    private val callback: OnDataLoadedCallback<T>
) : AsyncTask<T, Void, T?>() {
    private var exception: Exception? = null
    
    override fun doInBackground(vararg p0: T): T? =
        try {
            p0.first()
        } catch (exception: Exception) {
            this.exception = exception
            null
        }
    
    override fun onPostExecute(result: T?) {
        if (result == null) {
            exception?.let { callback.onDataNotAvailable(it) }
        } else {
            callback.onDataLoaded(result)
        }
    }
    
}
