package com.sun.heartrate.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sun.heartrate.R
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.ui.main.MainActivity
import com.sun.heartrate.utils.*
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.item_history_heartbeat.*

class HistoryFragment : Fragment(),
    MainActivity.OnMenuOptionCallBack,
    MonthHistoryAdapter.OnItemClickListener,
    DeleteHeartDialog.DeleteHeartDialogCallback,
    DetailHistoryAdapter.OnItemClickListener,
    View.OnClickListener {
    
    private val monthHistoryAdapter: MonthHistoryAdapter by lazy {
        MonthHistoryAdapter(heartsMonths, this)
    }
    private val detailHistoryAdapter: DetailHistoryAdapter by lazy {
        DetailHistoryAdapter(hearts, this)
    }
    
    private lateinit var heartModelLast: HeartModel
    private var hearts = mutableListOf<HeartModel>()
    private var heartsMonths = mutableListOf<String>()
    
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
        setHeartRateLastView()
    }
    
    private fun setHeartRateLastView() {
        heartModelLast = hearts[(hearts.size) - 1]
        textViewNumberRate?.text = heartModelLast.heartRate.toString()
        textViewNote?.text = heartModelLast.title
        textViewDate?.text = heartModelLast.time.formatDate()
        imageStatus?.setImageResource(heartModelLast.image)
    }
    
    private fun initListener() {
        assignViews(imageViewDelete)
    }
    
    override fun menuOptionCallBack(value: String) {
        showViewMenuOption(
            value,
            recyclerMonth,
            recyclerHeartDetail,
            includePartialFragmentHistory
        )
    }
    
    private fun showViewMenuOption(
        value: String,
        vararg views: View?
    ) {
        if (value == Constant.SHOW_ALL) {
            views[0]?.show()
            views[1]?.gone()
            views[2]?.show()
        } else {
            views[0]?.gone()
            views[1]?.show()
            views[2]?.gone()
        }
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
            setHasFixedSize(true)
            adapter = detailHistoryAdapter
        }
    }
    
    override fun loadDeleteHeartDialogCallback(heartModel: HeartModel, check: Int) {
        when (check) {
            DELETE_FROM_DETAIL_ADAPTER -> {
                hearts.remove(heartModel)
                detailHistoryAdapter.updateAdapter(hearts)
            }
            DELETE_FROM_MONTH_ADAPTER -> {
                hearts.remove(heartModel)
                monthHistoryAdapter.updateHeart(hearts)
            }
            DELETE_FROM_LAST_MEASURE -> {
                hearts.remove(heartModel)
                setHeartRateLastView()
            }
        }
    }
    
    override fun onDeleteHeart(heartModel: HeartModel, itemView: View) {
        context?.let {
            DeleteHeartDialog(
                it,
                this,
                heartModel,
                DELETE_FROM_DETAIL_ADAPTER
            ).show()
        }
    }
    
    override fun loadHeartMonthDetail(month: String, itemView: View?) {
        monthHistoryAdapter.getHeartByMonth(itemView, hearts)
    }
    
    override fun loadDeleteHeartMonthDetail(heartModel: HeartModel) {
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
    
    companion object {
        
        const val DELETE_FROM_LAST_MEASURE = 0
        const val DELETE_FROM_MONTH_ADAPTER = 2
        const val DELETE_FROM_DETAIL_ADAPTER = 1
        
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}
