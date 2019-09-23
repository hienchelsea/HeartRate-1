package com.sun.heartrate.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sun.heartrate.R
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.utils.Constant
import com.sun.heartrate.utils.assignViews
import kotlinx.android.synthetic.main.item_month.view.*

class MonthHistoryAdapter(
    private var heartMonths: MutableList<String>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MonthHistoryAdapter.ViewHolder>(),
    DetailHistoryAdapter.OnItemClickListener {
    
    lateinit var holder: ViewHolder
    private val viewPool = RecyclerView.RecycledViewPool()
    
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(
            viewGroup.context
        ).inflate(R.layout.item_month, viewGroup, false)
        return ViewHolder(
            itemView,
            onItemClickListener,
            this,
            viewPool
        )
    }
    
    override fun getItemCount() = heartMonths.size
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(heartMonths[position])
        this.holder = holder
    }
    
    fun getHeartByMonth(itemView: View?, hearts: List<HeartModel>) {
        holder.setDetailHistoryAdapter(itemView, hearts)
    }
    
    fun updateDetailHeart(hearts: List<HeartModel>) {
        holder.updateHistoryDetailAdapter(hearts)
    }
    
    fun updateAdapter(heartMonths: MutableList<String>) {
        this.heartMonths = heartMonths
        notifyDataSetChanged()
    }
    
    override fun onDeleteHeart(
        heartModel: HeartModel,
        heartMonths: List<HeartModel>,
        itemView: View
    ) {
        onItemClickListener.onDeleteHeartMonthDetail(heartModel, heartMonths)
    }
    
    class ViewHolder(
        itemView: View,
        private val onItemClickListener: OnItemClickListener,
        private val onItemClickListenerDetailHistoryAdapter: DetailHistoryAdapter.OnItemClickListener,
        private val viewPool: RecyclerView.RecycledViewPool
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var item: String? = null
        private val detailHistoryAdapter: DetailHistoryAdapter by lazy {
            DetailHistoryAdapter(
                listOf(),
                onItemClickListenerDetailHistoryAdapter
            )
        }
        
        init {
            assignViews(itemView.cardViewMonth)
            itemView.apply {
                recyclerDetail?.run {
                    layoutManager = StaggeredGridLayoutManager(
                        Constant.SPAN_COUNT,
                        StaggeredGridLayoutManager.VERTICAL
                    )
                    setHasFixedSize(true)
                    setRecycledViewPool(viewPool)
                    adapter = detailHistoryAdapter
                }
            }
        }
        
        override fun onClick(view: View?) {
            if (view?.id == R.id.cardViewMonth) {
                item?.let { onItemClickListener.loadHeartMonthDetail(it, itemView) }
            }
        }
        
        fun bindData(heartModel: String) {
            item = heartModel
            itemView.textViewMonth.text = item
        }
        
        fun setDetailHistoryAdapter(itemView: View?, hearts: List<HeartModel>) {
            detailHistoryAdapter.updateAdapter(hearts)
            itemView?.apply {
                recyclerDetail.adapter = detailHistoryAdapter
                showViewRecyclerDetail(this)
            }
        }
        
        fun updateHistoryDetailAdapter(hearts: List<HeartModel>) {
            detailHistoryAdapter.updateAdapter(hearts)
        }
        
        private fun showViewRecyclerDetail(itemView: View) {
            if (itemView.recyclerDetail.visibility == View.GONE)
                itemView.recyclerDetail.visibility = View.VISIBLE
            else itemView.recyclerDetail.visibility = View.GONE
        }
    }
    
    interface OnItemClickListener {
        fun loadHeartMonthDetail(month: String, itemView: View)
        fun onDeleteHeartMonthDetail(heartModel: HeartModel, heartMonths: List<HeartModel>)
    }
}
