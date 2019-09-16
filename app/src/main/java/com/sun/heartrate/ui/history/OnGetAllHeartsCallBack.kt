package com.sun.heartrate.ui.history

import com.sun.heartrate.data.model.OnDataLoadedCallback

class OnGetAllHeartsCallBack<T>(
    private val getData: (hearts: List<T>, exception: String)->Unit
): OnDataLoadedCallback<List<T>> {
    
    override fun onDataLoaded(data: List<T>) {
         getData(data, "")
    }
    
    override fun onDataNotAvailable(exception: Exception) {
        getData(emptyList(),exception.toString())
    }
}