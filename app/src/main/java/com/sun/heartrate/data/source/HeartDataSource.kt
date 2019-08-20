package com.sun.heartrate.data.source

import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.model.OnDataLoadedCallback

interface HeartDataSource {
    fun insetHeart(
        heartModel: HeartModel,
        onDataLoadedCallback: OnDataLoadedCallback<Boolean>
    )

    fun deleteHeart(
        heartModel: HeartModel,
        onDataLoadedCallback: OnDataLoadedCallback<Boolean>
    )

    fun getAllHearts(onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>)

    fun getHeartsByMonth(
        month: String,
        onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>
    )

    fun getHeartsByStatus(
        image: Int,
        onDataLoadedCallback: OnDataLoadedCallback<List<HeartModel>>
    )
}
