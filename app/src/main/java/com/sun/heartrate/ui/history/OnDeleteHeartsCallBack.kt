package com.sun.heartrate.ui.history

import com.sun.heartrate.data.model.OnDataLoadedCallback
import com.sun.heartrate.utils.Constant

class OnDeleteHeartsCallBack(
    private val onDeleteHeart: (values: Boolean, exception: String) -> Unit
) : OnDataLoadedCallback<Boolean> {
    override fun onDataLoaded(data: Boolean) {
        onDeleteHeart(data, Constant.NULL)
    }
    
    override fun onDataNotAvailable(exception: Exception) {
        onDeleteHeart(false, exception.toString())
    }
}
