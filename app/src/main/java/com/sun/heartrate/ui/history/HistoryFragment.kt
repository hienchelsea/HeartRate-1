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
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment(),
    MainActivity.OnMenuOptionCallBack {
    
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
        initAdapter()
    }
    
    override fun menuOptionCallBack(value: String) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    private fun initAdapter() {
        val hearts = mutableListOf<HeartModel>()
        val monthHistoryAdapter = MonthHistoryAdapter(hearts)
        recyclerMonth.run {
            layoutManager = StaggeredGridLayoutManager(
                1,
                StaggeredGridLayoutManager.VERTICAL
            )
            setHasFixedSize(true)
            adapter = monthHistoryAdapter
        }
    }
    
    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
    
}
