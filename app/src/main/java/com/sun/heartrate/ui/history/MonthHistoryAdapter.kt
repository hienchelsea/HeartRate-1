package com.sun.heartrate.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sun.heartrate.R
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.utils.gone
import com.sun.heartrate.utils.show
import kotlinx.android.synthetic.main.item_month.view.*

class MonthHistoryAdapter(
    private val heartMonths: MutableList<HeartModel>
) : RecyclerView.Adapter<MonthHistoryAdapter.ViewHolder>() {
    
    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView = LayoutInflater.from(
            viewGroup.context
        ).inflate(R.layout.item_month, viewGroup, false)
        return ViewHolder(itemView)
    }
    
    override fun getItemCount() = heartMonths.size
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(heartMonths[position])
    }
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        
        private lateinit var item: HeartModel
        private var hearts = mutableListOf<HeartModel>()
        
        private val detailHistoryFragment: DetailHistoryAdapter by lazy {
            DetailHistoryAdapter(hearts)
        }
        
        init {
            itemView.cardViewMonth.setOnClickListener(this)
        }
        
        override fun onClick(view: View?) {
            if (view?.id == R.id.cardViewMonth) {
                itemView.recyclerDetail.run {
                    layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
                    setHasFixedSize(true)
                    adapter = detailHistoryFragment
                }
                setVisibilityUi(itemView)
            }
        }
        
        fun bindData(heartModel: HeartModel) {
            item = heartModel
            itemView.textViewMonth?.text = item.monthYear
        }
        
        private fun setVisibilityUi(itemView: View?) {
            itemView?.apply {
                if (recyclerDetail?.visibility == View.GONE) {
                    imageView.setImageResource(R.drawable.ic_down)
                    recyclerDetail?.show()
                } else {
                    imageView.setImageResource(R.drawable.ic_next)
                    recyclerDetail?.gone()
                }
            }
        }
    }
}
