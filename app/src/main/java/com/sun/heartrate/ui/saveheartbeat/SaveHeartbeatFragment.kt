package com.sun.heartrate.ui.saveheartbeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sun.heartrate.R
import com.sun.heartrate.utils.assignViews
import com.sun.heartrate.utils.createProgressBarSaveHeartPercent
import kotlinx.android.synthetic.main.fragment_save_heartbeat.*

class SaveHeartbeatFragment(
    private val onBackPressed: OnBackHeartbeatFragment
) : Fragment(),
    View.OnClickListener {
    
    var isStatusLast = GENERAL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_save_heartbeat, container, false)
    
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }
    
    private fun initView() {
        textRateNumberSave.text = arguments?.getInt(NUMBER_RATE).toString() + BMP
        textViewTime.text = arguments?.getString(MEASUREMENT_TIME).toString()
        progressBarSaveHeart.progress = createProgressBarSaveHeartPercent(
            arguments?.getInt(NUMBER_RATE)!!,
            NUMBER_RATE_MIN, NUMBER_RATE_MAX
        )
    }
    
    private fun initListener() {
        assignViews(
            buttonSave,
            imageViewBack,
            imageViewBeforeTraining,
            imageViewBeforeTraining,
            imageViewAfterTraining,
            imageViewGeneral,
            imageViewHeavyTraining,
            imageViewRested
        )
    }
    
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imageViewBack -> onBackPressed.backFragment()
            R.id.buttonSave -> {
                saveDataThenBack()
            }
            R.id.imageViewBeforeTraining -> selectStatus(BEFORE_TRAINING, isStatusLast)
            R.id.imageViewAfterTraining -> selectStatus(AFTER_TRAINING, isStatusLast)
            R.id.imageViewGeneral -> selectStatus(GENERAL, isStatusLast)
            R.id.imageViewHeavyTraining -> selectStatus(HEAVY_TRAINING, isStatusLast)
            R.id.imageViewRested -> selectStatus(RESTED, isStatusLast)
        }
    }
    
    private fun saveDataThenBack() {
        //add to sql
        onBackPressed.backFragment()
    }
    
    private fun selectStatus(isStatusCurrent: Int, isStatusLast: Int) {
        when (isStatusCurrent) {
            BEFORE_TRAINING ->
                imageViewBeforeTraining.setImageResource(R.drawable.ic_before_training_red)
            AFTER_TRAINING ->
                imageViewAfterTraining.setImageResource(R.drawable.ic_after_training_red)
            GENERAL ->
                imageViewGeneral.setImageResource(R.drawable.ic_general_red)
            HEAVY_TRAINING ->
                imageViewHeavyTraining.setImageResource(R.drawable.ic_heavy_training_red)
            RESTED ->
                imageViewRested.setImageResource(R.drawable.ic_rested_red)
        }
        
        when (isStatusLast) {
            BEFORE_TRAINING ->
                imageViewBeforeTraining.setImageResource(R.drawable.ic_before_training_dark)
            AFTER_TRAINING ->
                imageViewAfterTraining.setImageResource(R.drawable.ic_after_training_dark)
            GENERAL ->
                imageViewGeneral.setImageResource(R.drawable.ic_general_dark)
            HEAVY_TRAINING ->
                imageViewHeavyTraining.setImageResource(R.drawable.ic_heavy_training_dark)
            RESTED ->
                imageViewRested.setImageResource(R.drawable.ic_rested_dark)
        }
        this.isStatusLast = isStatusCurrent
    }
    
    companion object {
        
        const val BEFORE_TRAINING = 1
        const val AFTER_TRAINING = 2
        const val GENERAL = 3
        const val HEAVY_TRAINING = 4
        const val RESTED = 5
        const val NUMBER_RATE_MAX = 150
        const val NUMBER_RATE_MIN = 30
        const val NUMBER_RATE = "NumberRate"
        const val MEASUREMENT_TIME = "MeasurementTime"
        const val BMP = " BMP"
        
        @JvmStatic
        fun newInstance(
            onBackPressed: OnBackHeartbeatFragment,
            numberRate: Int, measurementTime: String
        ): SaveHeartbeatFragment {
            return SaveHeartbeatFragment(onBackPressed).apply {
                arguments = Bundle().apply {
                    putInt(NUMBER_RATE, numberRate)
                    putString(MEASUREMENT_TIME, measurementTime)
                }
            }
        }
    }
    
    interface OnBackHeartbeatFragment {
        fun backFragment()
    }
    
}
