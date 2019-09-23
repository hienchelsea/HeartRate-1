package com.sun.heartrate.ui.saveheartbeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sun.heartrate.R
import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.model.HeartModel
import com.sun.heartrate.data.repository.HeartRepository
import com.sun.heartrate.data.source.HeartLocalDataSource
import com.sun.heartrate.utils.assignViews
import com.sun.heartrate.utils.createProgressPercent
import com.sun.heartrate.utils.formatDate
import com.sun.heartrate.utils.formatMonth
import kotlinx.android.synthetic.main.fragment_save_heartbeat.*

class SaveHeartbeatFragment(
    private val onBackPressed: OnBackHeartbeatFragment
) : Fragment(),
    View.OnClickListener,
    SaveHeartbeatContract.View {
    
    private var isStatusLast = GENERAL
    private var imageId = R.drawable.ic_general_red
    
    private val heartModel: HeartModel by lazy {
        HeartModel(
            ID,
            editTextNote.text.toString(),
            arguments?.getInt(NUMBER_RATE)!!,
            imageId,
            (arguments?.getLong(MEASUREMENT_TIME)!!).formatMonth(),
            arguments?.getLong(MEASUREMENT_TIME)!!)
    }
    private val saveHeartbeatPresenter: SaveHeartbeatContract.Presenter by lazy {
        SaveHeartbeatPresenter(heartRepository, this)
    }
    
    private val heartRepository: HeartRepository by lazy {
        HeartRepository(heartLocalDataSource)
    }
    
    private val heartLocalDataSource: HeartLocalDataSource by lazy {
        HeartLocalDataSource(heartDatabase)
    }
    
    private val heartDatabase: HeartDatabase by lazy {
        HeartDatabase(context)
    }
    
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
        textViewTime.text = (arguments?.getLong(MEASUREMENT_TIME)!!).formatDate()
        progressBarSaveHeart.progress =
            createProgressPercent(arguments?.getInt(NUMBER_RATE)!!, NUMBER_RATE_MAX)
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
    
    override fun showToastNotification(value: Boolean) {
        var textShow = getString(R.string.title_add_data_successfully)
        if (!value) textShow = getString(R.string.title_add_data_failed)
        Toast.makeText(
            context,
            textShow,
            Toast.LENGTH_LONG
        ).show()
        
        backHeartFragment()
    }
    
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.imageViewBack -> backHeartFragment()
            R.id.buttonSave -> saveDataHeart()
            R.id.imageViewBeforeTraining -> selectStatus(BEFORE_TRAINING, isStatusLast)
            R.id.imageViewAfterTraining -> selectStatus(AFTER_TRAINING, isStatusLast)
            R.id.imageViewGeneral -> selectStatus(GENERAL, isStatusLast)
            R.id.imageViewHeavyTraining -> selectStatus(HEAVY_TRAINING, isStatusLast)
            R.id.imageViewRested -> selectStatus(RESTED, isStatusLast)
        }
    }
    
    private fun saveDataHeart() {
        saveHeartbeatPresenter.saveHeartbeat(heartModel)
    }
    
    private fun backHeartFragment() {
        onBackPressed.backFragment()
    }
    
    private fun selectStatus(isStatusCurrent: Int, isStatusLast: Int) {
        when (isStatusCurrent) {
            BEFORE_TRAINING -> setImageStatus(
                R.drawable.ic_before_training_red,
                imageViewBeforeTraining
            )
            AFTER_TRAINING -> setImageStatus(
                R.drawable.ic_after_training_red,
                imageViewAfterTraining
            )
            GENERAL -> setImageStatus(
                R.drawable.ic_general_red,
                imageViewGeneral
            )
            HEAVY_TRAINING -> setImageStatus(
                R.drawable.ic_heavy_training_red,
                imageViewHeavyTraining
            )
            RESTED -> setImageStatus(
                R.drawable.ic_rested_red,
                imageViewRested
            )
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
    
    private fun setImageStatus(idImage: Int, image: ImageView) {
        image.setImageResource(idImage)
        this.imageId = idImage
    }
    
    companion object {
        
        const val BEFORE_TRAINING = 1
        const val AFTER_TRAINING = 2
        const val GENERAL = 3
        const val HEAVY_TRAINING = 4
        const val RESTED = 5
        const val NUMBER_RATE_MAX = 150
        const val NUMBER_RATE = "NumberRate"
        const val MEASUREMENT_TIME = "MeasurementTime"
        const val BMP = " BMP"
        const val ID = 0
        
        @JvmStatic
        fun newInstance(
            onBackPressed: OnBackHeartbeatFragment,
            numberRate: Int, measurementTime: Long
        ): SaveHeartbeatFragment {
            return SaveHeartbeatFragment(onBackPressed).apply {
                arguments = Bundle().apply {
                    putInt(NUMBER_RATE, numberRate)
                    putLong(MEASUREMENT_TIME, measurementTime)
                }
            }
        }
    }
    
    interface OnBackHeartbeatFragment {
        fun backFragment()
    }
}
