package com.sun.heartrate.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sun.heartrate.R
import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.repository.HeartRepository
import com.sun.heartrate.data.source.HeartLocalDataSource
import com.sun.heartrate.ui.main.MainActivity
import com.sun.heartrate.utils.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.item_history_heartbeat.*

class HistoryFragment : Fragment(),
    HistoryContract.View,
    MainActivity.OnMenuOptionCallBack,
    MonthHistoryAdapter.OnItemClickListener,
    View.OnClickListener,
    DeleteHeartDialog.DeleteHeartDialogCallback,
    DetailHistoryAdapter.OnItemClickListener {
    
    private val historyPresenter: HistoryContract.Presenter by lazy {
        HistoryPresenter(heartRepository, this)
    }
    
    private val heartDatabase: HeartDatabase by lazy {
        HeartDatabase(context)
    }
    
    private val heartLocalDataSource: HeartLocalDataSource by lazy {
        HeartLocalDataSource(heartDatabase)
    }
    
    private val heartRepository: HeartRepository by lazy {
        HeartRepository(heartLocalDataSource)
    }
    
    private val monthHistoryAdapter: MonthHistoryAdapter by lazy {
        MonthHistoryAdapter(heartsMonths, this)
    }
    private val detailHistoryAdapter: DetailHistoryAdapter by lazy {
        DetailHistoryAdapter(this.hearts, this)
    }
    
    private var heartsMonths = mutableListOf<String>()
    private var hearts = mutableListOf<HeartModel>()
    private lateinit var heartModelLast: HeartModel
    private lateinit var itemView: View
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_history, container, false)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initAdapter()
    }
    
    private fun initAdapter() {
        recyclerMonth.run {
            layoutManager = StaggeredGridLayoutManager(
                Constant.SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = monthHistoryAdapter
        }
        recyclerHeartDetail.run {
            layoutManager = StaggeredGridLayoutManager(
                Constant.SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL
            )
            adapter = detailHistoryAdapter
        }
    }
    
    private fun initListener() {
        assignViews(imageViewDelete)
    }
    
    private fun setHeartRateLastView() {
        if (this.hearts.size > 0) {
            includePartialFragmentHistory.show()
            heartModelLast = this.hearts[(this.hearts.size) - 1]
            textViewNumberRate?.text = heartModelLast.heartRate.toString()
            textViewNote?.text = heartModelLast.title
            textViewDate?.text = heartModelLast.time.formatDate()
            imageStatus?.setImageResource(heartModelLast.image)
        } else includePartialFragmentHistory.gone()
    }
    
    override fun menuOptionCallBack(value: String) {
        showViewMenuOption(
            value,
            recyclerMonth,
            recyclerHeartDetail,
            includePartialFragmentHistory
        )
        getDataByMenuOption(value)
    }
    
    private fun showViewMenuOption(
        value: String,
        vararg views: View?
    ) {
        views[2]?.gone()
        if (value == Constant.SHOW_ALL) {
            views[0]?.show()
            views[1]?.gone()
        } else {
            views[0]?.gone()
            views[1]?.show()
        }
    }
    
    private fun getDataByMenuOption(value: String) {
        if (value == Constant.SHOW_ALL) historyPresenter.getAllHeartbeat()
        else historyPresenter.getHeartbeatByStatus(getStatusId(value))
    }
    
    override fun onError(exception: String) {
        //Log.d("History", exception)
    }
    
    override fun displayListHeartbeat(
        hearts: List<HeartModel>,
        heartsMonths: List<String>
    ) {
        this.hearts = hearts.toMutableList()
        this.heartsMonths = heartsMonths.toMutableList()
        if (hearts.isNotEmpty()) {
            setHeartRateLastView()
            monthHistoryAdapter.updateAdapter(heartsMonths.toMutableList())
            detailHistoryAdapter.updateAdapter(hearts.toMutableList())
        }
    }
    
    override fun displayListHeartbeatByStatus(hearts: List<HeartModel>) {
        this.hearts = hearts.toMutableList()
        detailHistoryAdapter.updateAdapter(hearts.toMutableList())
    }
    
    override fun displayListHeartbeatByMonth(month: String, hearts: List<HeartModel>) {
        if (hearts.isEmpty()) {
            heartsMonths.remove(month)
            monthHistoryAdapter.updateAdapter(heartsMonths)
        } else monthHistoryAdapter.getHeartByMonth(itemView, hearts)
    }
    
    override fun confirmDeleted(successful: Boolean, heartModel: HeartModel, check: Int) {
        if (successful) {
            hearts.remove(heartModel)
            when (check) {
                DELETE_FROM_DETAIL_ADAPTER -> detailHistoryAdapter.updateAdapter(this.hearts)
                DELETE_FROM_MONTH_ADAPTER -> deleteFromMonthAdapter(heartModel)
                DELETE_FROM_LAST_MEASURE -> historyPresenter.getHeartbeatByMonth(heartModel.monthYear)
            }
            if (heartModelLast == heartModel) {
                setHeartRateLastView()
            }
        }
    }
    
    private fun deleteFromMonthAdapter(heartModel: HeartModel) {
        hearts.remove(heartModel)
        monthHistoryAdapter.updateDetailHeart(this.hearts)
        if (hearts.isEmpty()) {
            heartsMonths.remove(heartModel.monthYear)
            monthHistoryAdapter.updateAdapter(heartsMonths)
        }
    }
    
    override fun onDeleteHeart(
        heartModel: HeartModel,
        heartMonths: List<HeartModel>,
        itemView: View
    ) {
        context?.let {
            DeleteHeartDialog(
                it,
                this,
                heartModel,
                DELETE_FROM_DETAIL_ADAPTER
            ).show()
        }
    }
    
    override fun loadDeleteHeartDialogCallback(
        heartModel: HeartModel,
        check: Int
    ) {
        historyPresenter.deleteHeartbeat(heartModel, check)
    }
    
    
    override fun loadHeartMonthDetail(
        month: String,
        itemView: View
    ) {
        historyPresenter.getHeartbeatByMonth(month)
        this.itemView = itemView
    }
    
    override fun onDeleteHeartMonthDetail(
        heartModel: HeartModel,
        heartMonths: List<HeartModel>
    ) {
        this.hearts = heartMonths.toMutableList()
        context?.let {
            DeleteHeartDialog(
                it,
                this,
                heartModel,
                DELETE_FROM_MONTH_ADAPTER
            ).show()
        }
    }
    
    override fun onClick(view: View?) {
        if (view?.id == R.id.imageViewDelete)
            context?.let {
                DeleteHeartDialog(
                    it,
                    this,
                    heartModelLast,
                    DELETE_FROM_LAST_MEASURE
                ).show()
            }
    }
    
    override fun onResume() {
        historyPresenter.getAllHeartbeat()
        super.onResume()
    }
    
    companion object {
        
        const val DELETE_FROM_LAST_MEASURE = 0
        const val DELETE_FROM_MONTH_ADAPTER = 2
        const val DELETE_FROM_DETAIL_ADAPTER = 1
        
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}
