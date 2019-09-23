package com.sun.heartrate.ui.history

import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.repository.HeartRepository
import com.sun.heartrate.utils.Constant
import com.sun.heartrate.utils.getHeartsMonths

class HistoryPresenter(
    private val heartRepository: HeartRepository,
    private val view: HistoryContract.View
) : HistoryContract.Presenter {
    
    override fun getAllHeartbeat() {
        heartRepository.getAllHearts(
            OnGetAllHeartsCallBack { hearts: List<HeartModel>,
                                     exception: String ->
                getHeartbeatResult(hearts, exception)
            })
    }
    
    override fun getHeartbeatByStatus(status: Int) {
        heartRepository.getHeartsByStatus(
            status,
            OnGetAllHeartsCallBack { hearts: List<HeartModel>,
                                     exception: String ->
                getHeartbeatStatus(hearts, exception)
            }
        
        )
    }
    
    override fun getHeartbeatByMonth(month: String) {
        heartRepository.getHeartsByMonth(
            month,
            OnGetAllHeartsCallBack { hearts: List<HeartModel>,
                                     exception: String ->
                getHeartbeatMonth(month, hearts, exception)
            }
        )
    }
    
    override fun deleteHeartbeat(heartModel: HeartModel, check: Int) {
        heartRepository.deleteHeart(
            heartModel,
            OnDeleteHeartsCallBack { values: Boolean, exception: String ->
                loadDeleteHeartbeat(values, exception, heartModel, check)
            })
    }
    
    private fun loadDeleteHeartbeat(
        values: Boolean,
        exception: String,
        heartModel: HeartModel,
        check: Int
    ) {
        if (values) {
            view.confirmDeleted(values, heartModel, check)
        } else {
            view.onError(exception)
        }
    }
    
    private fun getHeartbeatResult(
        hearts: List<HeartModel>,
        exception: String
    ) {
        if (exception == Constant.NULL && hearts.isNotEmpty()) {
            view.displayListHeartbeat(hearts, getHeartsMonths(hearts))
        } else {
            view.onError(exception)
        }
    }
    
    private fun getHeartbeatStatus(
        hearts: List<HeartModel>,
        exception: String
    ) {
        if (exception == Constant.NULL) {
            view.displayListHeartbeatByStatus(hearts)
        } else {
            view.onError(exception)
        }
    }
    
    private fun getHeartbeatMonth(
        month: String,
        hearts: List<HeartModel>,
        exception: String
    ) {
        if (exception == Constant.NULL) {
            view.displayListHeartbeatByMonth(month, hearts)
        } else {
            view.onError(exception)
        }
    }
}
