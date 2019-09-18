package com.sun.heartrate.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.heartrate.R
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.utils.formatDate
import kotlinx.android.synthetic.main.item_history_heartbeat.view.*

class DetailHistoryAdapter(
    private val heartMonths: MutableList<HeartModel>
) : RecyclerView.Adapter<DetailHistoryAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(
            viewGroup.context
        ).inflate(R.layout.item_history_heartbeat, viewGroup, false)
        return ViewHolder(itemView)
    }
    
    override fun getItemCount() = heartMonths.size
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(heartMonths[position])
    }
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var item: HeartModel
        
        fun bindData(heartModel: HeartModel) {
            item = heartModel
            itemView.apply {
                textViewNumberRate?.text = item.heartRate.toString()
                textViewDate.text = item.time.formatDate()
                textViewNote?.text = item.title
                imageStatus?.setImageResource(item.image)
            }
        }
    }
}
