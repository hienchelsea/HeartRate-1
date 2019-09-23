package com.sun.heartrate.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sun.heartrate.R
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.utils.assignViews
import com.sun.heartrate.utils.formatDate
import kotlinx.android.synthetic.main.item_history_heartbeat.view.*

class DetailHistoryAdapter(
    private var heartMonths: List<HeartModel>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DetailHistoryAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(
        viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(
            viewGroup.context
        ).inflate(R.layout.item_history_heartbeat, viewGroup, false)
        return ViewHolder(itemView, onItemClickListener)
    }
    
    override fun getItemCount() = heartMonths.size
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(heartMonths[position], heartMonths)
    }
    
    fun updateAdapter(heartMonths: List<HeartModel>) {
        val diffUtil = DiffUtil.calculateDiff(
            HeartUpdateCallback(this.heartMonths, heartMonths)
        )
        this.heartMonths = heartMonths
        diffUtil.dispatchUpdatesTo(this)
        //   notifyDataSetChanged()
    }
    
    class ViewHolder(
        itemView: View,
        private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        
        private lateinit var item: HeartModel
        private lateinit var heartMonths: List<HeartModel>
        
        init {
            assignViews(itemView.imageViewDelete)
        }
        
        fun bindData(heartModel: HeartModel, heartMonths: List<HeartModel>) {
            item = heartModel
            this.heartMonths = heartMonths
            itemView.apply {
                textViewNumberRate?.text = item.heartRate.toString()
                textViewDate?.text = item.time.formatDate()
                textViewNote?.text = item.title
                imageStatus?.setImageResource(item.image)
            }
        }
        
        override fun onClick(view: View?) {
            if (view?.id == R.id.imageViewDelete) {
                onItemClickListener.onDeleteHeart(item, heartMonths, itemView)
            }
        }
    }
    
    class HeartUpdateCallback(
        private val oldHearts: List<HeartModel>,
        private val newHearts: List<HeartModel>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldHearts.size
        
        override fun getNewListSize() = newHearts.size
        
        override fun areItemsTheSame(oldPosition: Int, newPosition: Int) =
            oldHearts[oldPosition].id == newHearts[newPosition].id
        
        override fun areContentsTheSame(oldPosition: Int, newPosition: Int) =
            areItemsTheSame(oldPosition, newPosition)
    }
    
    interface OnItemClickListener {
        fun onDeleteHeart(heartModel: HeartModel, heartMonths: List<HeartModel>, itemView: View)
    }
}
