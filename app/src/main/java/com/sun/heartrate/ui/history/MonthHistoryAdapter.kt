package com.sun.heartrate.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sun.heartrate.R
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.utils.assignViews
import com.sun.heartrate.utils.gone
import com.sun.heartrate.utils.show
import kotlinx.android.synthetic.main.item_month.view.*

class MonthHistoryAdapter(
    private var heartMonths: MutableList<String>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<MonthHistoryAdapter.ViewHolder>(),
    DetailHistoryAdapter.OnItemClickListener {
    
    private lateinit var holder: ViewHolder
    
    override fun onDeleteHeart(heartModel: HeartModel, heartMonths: List<HeartModel>, itemView: View) {
        onItemClickListener.loadDeleteHeartMonthDetail(heartModel,heartMonths)
    }
    
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(
            viewGroup.context
        ).inflate(R.layout.item_month, viewGroup, false)
        return ViewHolder(itemView, onItemClickListener, this)
    }
    
    override fun getItemCount() = heartMonths.size
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(heartMonths[position])
        this.holder = holder
    }
    
    fun getHeartByMonth(itemView: View, hearts: List<HeartModel>) {
        holder.setDetailHistoryAdapter(itemView, hearts)
    }
    
    fun updateDetailHeart(hearts: List<HeartModel>) {
        holder.updateHistoryDetailAdapter(hearts)
    }
    
    class ViewHolder(
        itemView: View,
        private val onItemClickListener: OnItemClickListener,
        private val onItemClickListenerDetailHistoryAdapter: DetailHistoryAdapter.OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var item: String? = null
        private lateinit var detailHistoryAdapter: DetailHistoryAdapter
        
        init {
            assignViews(itemView.cardViewMonth)
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
            detailHistoryAdapter = DetailHistoryAdapter(hearts, onItemClickListenerDetailHistoryAdapter)
            itemView?.recyclerDetail?.run {
                layoutManager = StaggeredGridLayoutManager(
                    1, StaggeredGridLayoutManager.VERTICAL
                )
                setHasFixedSize(true)
                adapter = detailHistoryAdapter
            }
            itemView?.let { setVisibilityUi(it) }
        }
        
        fun updateHistoryDetailAdapter(hearts: List<HeartModel>) {
            detailHistoryAdapter.updateAdapter(hearts)
        }
        
        private fun setVisibilityUi(itemView: View) {
            if (itemView.recyclerDetail.visibility == View.GONE)
                itemView.recyclerDetail.show()
            else itemView.recyclerDetail.gone()
        }
    }
    
    fun updateAdapter(heartMonths: MutableList<String>) {
        this.heartMonths = heartMonths
        notifyDataSetChanged()
    }
    
    interface OnItemClickListener {
        fun loadHeartMonthDetail(month: String, itemView: View)
        fun loadDeleteHeartMonthDetail(heartModel: HeartModel,heartMonths: List<HeartModel>)
    }
}