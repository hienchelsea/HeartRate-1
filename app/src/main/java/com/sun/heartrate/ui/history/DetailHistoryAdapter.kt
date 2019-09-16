package com.sun.heartrate.ui.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.heartrate.R
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.utils.assignViews
import kotlinx.android.synthetic.main.item_history_heartbeat.view.*
import java.text.SimpleDateFormat

class DetailHistoryAdapter(
    private var hearts: List<HeartModel>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DetailHistoryAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(
            viewGroup.context
        ).inflate(R.layout.item_history_heartbeat, viewGroup, false)
        return ViewHolder(itemView, onItemClickListener)
    }
    
    override fun getItemCount() = hearts.size
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(hearts[position], hearts)
    }
    
    class ViewHolder(itemView: View,
                     private val onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        
        private lateinit var item: HeartModel
        private lateinit var heartMonths: List<HeartModel>
        private val formatDate = SimpleDateFormat("HH:mm:ss dd-MM-yyyy")
        
        init {
            assignViews(itemView.imageViewDelete)
        }
        
        @SuppressLint("SetTextI18n")
        fun bindData(heartModel: HeartModel, heartMonths: List<HeartModel>) {
            item = heartModel
            this.heartMonths = heartMonths
            itemView.textViewNumberRate?.text = item.heartRate.toString() + " BMP"
            itemView.textViewDate?.text = formatDate.format(item.time)
            itemView.textViewNote?.text = item.title
            itemView.imageStatus?.setImageResource(item.image)
        }
        
        override fun onClick(view: View?) {
            if (view?.id == R.id.imageViewDelete) {
                onItemClickListener.onDeleteHeart(item, heartMonths, itemView)
            }
        }
    }
    
    fun updateAdapter(heartMonths: List<HeartModel>) {
        this.hearts = heartMonths
        notifyDataSetChanged()
    }
    
    interface OnItemClickListener {
        fun onDeleteHeart(heartModel: HeartModel, heartMonths: List<HeartModel>, itemView: View)
    }
}