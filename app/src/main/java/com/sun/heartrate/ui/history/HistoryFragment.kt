package com.sun.heartrate.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
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
import java.text.SimpleDateFormat

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
    private val simpleDateFormat = SimpleDateFormat("HH:mm:ss dd-MM-yyyy")
    
    private val monthHistoryAdapter: MonthHistoryAdapter by lazy {
        MonthHistoryAdapter(heartsMonths, this)
    }
    private val detailHistoryAdapter: DetailHistoryAdapter by lazy {
        DetailHistoryAdapter(hearts, this)
    }
    private var heartsMonths = mutableListOf<String>()
    private var heartsMonthsDetail = mutableListOf<HeartModel>()
    private lateinit var heartModelLast: HeartModel
    private lateinit var itemView: View
    private var hearts = mutableListOf<HeartModel>()
    
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
    
    @SuppressLint("SetTextI18n")
    private fun setHeartRateLastView() {
        if (hearts.size > 0) {
            includePartialFragmentHistory.show()
            heartModelLast = hearts[(hearts.size) - 1]
            textViewNumberRate.text = heartModelLast.heartRate.toString() + " BMP"
            textViewNote.text = heartModelLast.title
            textViewDate.text = simpleDateFormat.format(heartModelLast.time)
            imageStatus.setImageResource(heartModelLast.image)
        } else {
            includePartialFragmentHistory.gone()
        }
    }
    
    private fun initListener() {
        assignViews(imageViewDelete)
    }
    
    override fun menuOptionCallBack(value: String) {
        setVisibilityViewUi(
            value,
            recyclerMonth,
            recyclerHeartDetail,
            includePartialFragmentHistory
        )
    }
    
    private fun setVisibilityViewUi(
        value: String,
        view: RecyclerView,
        view1: RecyclerView,
        view2: View
    ) {
        if (value == Constant.SHOW_ALL) {
            view.show()
            view1.gone()
            view2.show()
            historyPresenter.getAllHeartbeat()
        } else {
            view.gone()
            view1.show()
            view2.gone()
            historyPresenter.getHeartbeatByStatus(getIdStatus(value))
        }
    }
    
    private fun initAdapter() {
        recyclerMonth.run {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = monthHistoryAdapter
        }
        recyclerHeartDetail.run {
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = detailHistoryAdapter
        }
    }
    
    override fun onError(exception: String) {
        //Log.d("History", exception)
    }
    
    override fun displayListHeartbeat(
        hearts: List<HeartModel>,
        heartsMonths: MutableList<String>
    ) {
        this.hearts = hearts.toMutableList()
        this.heartsMonths = heartsMonths
        if (hearts.isNotEmpty()) {
            setHeartRateLastView()
            monthHistoryAdapter.updateAdapter(heartsMonths)
            detailHistoryAdapter.updateAdapter(hearts)
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
                DELETE_FROM_DETAIL_ADAPTER -> {
                    detailHistoryAdapter.updateAdapter(hearts)
                }
                DELETE_FROM_MONTH_ADAPTER -> {
                    heartsMonthsDetail.remove(heartModel)
                    monthHistoryAdapter.updateDetailHeart(hearts)
                    if (heartsMonthsDetail.isEmpty()) {
                        heartsMonths.remove(heartModel.monthYear)
                        monthHistoryAdapter.updateAdapter(heartsMonths)
                    }
                }
                DELETE_FROM_LAST_MEASUE -> {
                    historyPresenter.getHeartbeatByMonth(heartModel.monthYear)
                }
            }
            if (heartModelLast == heartModel) {
                setHeartRateLastView()
            }
        }
    }
    
    override fun loadDeleteHeartDialogCallback(heartModel: HeartModel, check: Int) {
        historyPresenter.deleteHeartbeat(heartModel, check)
    }
    
    override fun onDeleteHeart(
        heartModel: HeartModel,
        heartMonths: List<HeartModel>,
        itemView: View
    ) {
        context?.let {
            DeleteHeartDialog(
                this
            ).deleteHeartDialog(it, heartModel, DELETE_FROM_DETAIL_ADAPTER)
        }
    }
    
    override fun loadHeartMonthDetail(month: String, itemView: View) {
        historyPresenter.getHeartbeatByMonth(month)
        this.itemView = itemView
    }
    
    override fun loadDeleteHeartMonthDetail(
        heartModel: HeartModel,
        heartMonths: List<HeartModel>
    ) {
        heartsMonthsDetail = heartMonths.toMutableList()
        context?.let {
            DeleteHeartDialog(
                this
            ).deleteHeartDialog(it, heartModel, DELETE_FROM_MONTH_ADAPTER)
        }
    }
    
    override fun onClick(view: View?) {
        if (view?.id == R.id.imageViewDelete)
            context?.let {
                DeleteHeartDialog(
                    this
                ).deleteHeartDialog(it, heartModelLast, DELETE_FROM_LAST_MEASUE)
            }
    }
    
    override fun onResume() {
        historyPresenter.getAllHeartbeat()
        super.onResume()
    }
    
    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
        
        const val DELETE_FROM_LAST_MEASUE = 0
        const val DELETE_FROM_MONTH_ADAPTER = 2
        const val DELETE_FROM_DETAIL_ADAPTER = 1
    }
}
