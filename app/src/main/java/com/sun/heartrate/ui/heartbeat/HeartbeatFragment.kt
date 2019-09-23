package com.sun.heartrate.ui.heartbeat

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.sun.heartrate.R
import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.repository.HeartRepository
import com.sun.heartrate.data.source.HeartLocalDataSource
import com.sun.heartrate.ui.heartbeat.camera.CameraHelper
import com.sun.heartrate.ui.saveheartbeat.SaveHeartbeatFragment
import com.sun.heartrate.utils.CountDownProgressBar
import com.sun.heartrate.utils.createProgressPercent
import com.sun.heartrate.utils.formatDecimal
import kotlinx.android.synthetic.main.fragment_heartbeat.*

class HeartbeatFragment(
    private val onLoadFragment: OnLoadFragment
) : Fragment(),
    HeartbeatContract.View,
    View.OnClickListener,
    CameraHelper.OnDataLoadImageCallback,
    SaveHeartbeatFragment.OnBackHeartbeatFragment {
    
    private val heartbeatPresenter: HeartbeatContract.Presenter by lazy {
        HeartbeatPresenter(this)
    }
    
    private val heartDatabase: HeartDatabase by lazy {
        HeartDatabase(context)
    }
    
    private val heartLocalDataSource: HeartLocalDataSource by lazy {
        HeartLocalDataSource(heartDatabase)
    }
    
    private val heartRepository: HeartRepository by lazy {
        HeartRepository(heartLocalDataSource)
    }
    
    private val countDownProgressBar = CountDownProgressBar(MEASUREMENT_TIME) {
        updateProgressBar(
            createProgressPercent(
                getOpeningCameraTime(),
                MEASUREMENT_TIME.toInt()
            )
        )
    }
    
    private val cameraHelper by lazy {
        CameraHelper(
            activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager,
            this
        )
    }
    
    private var cameraBootTime = 0L
    private var rateNumber = 0L
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_heartbeat, container, false)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    
    override fun loadDataImage(
        value: ByteArray,
        widthImage: Int,
        heightImage: Int,
        timeStart: Long
    ) {
        heartbeatPresenter.getDataImage(value, widthImage, heightImage, timeStart)
    }
    
    private fun initView() {
        buttonStart?.setOnClickListener(this)
    }
    
    private fun openCamera() {
        cameraHelper.openCamera()
        cameraBootTime = System.currentTimeMillis()
        updateUiScreenHeartbeat()
    }
    
    override fun closeCamera() {
        cameraHelper.closeCamera()
        updateUiScreenHeartbeat()
    }
    
    private fun getOpeningCameraTime() = (System.currentTimeMillis() - cameraBootTime).toInt()
    
    private fun isRecording(): Boolean = cameraHelper.checkCamera()
    
    private fun requestCameraPermissions() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.CAMERA),
                CODE_PERMISSION_CAMERA
            )
        }
    }
    
    private fun updateUiScreenHeartbeat() {
        heartbeatPresenter.run {
            showButtonLabel(isRecording())
            displayImageViewHeart(isRecording())
            restartCountDownProgressBar(isRecording())
        }
        displayHeatRate(0)
        displayGuideline(R.string.title_measuring_heart_rate)
    }
    
    private fun showButtonLabel(isRecording: Boolean) {
        buttonStart?.text = if (isRecording) getText(R.string.label_pause) else getText(R.string.label_start)
    }
    
    private fun restartCountDownProgressBar(isRecording: Boolean) {
        countDownProgressBar.cancel()
        updateProgressBar(0)
        if (isRecording) countDownProgressBar.start()
    }
    
    private fun updateProgressBar(progressPercent: Int) {
        progressBarTime?.progress = progressPercent
        nextToSaveHeartBeatFragment(progressPercent, rateNumber.toInt())
    }
    
    override fun displayHeatRate(rateNumber: Int) {
        textRateNumber?.text = rateNumber.formatDecimal()
        this.rateNumber = rateNumber.toLong()
    }
    
    private fun displayImageViewHeart(isRecording: Boolean) {
        imageHeart?.run {
            if (isRecording) {
                setImageResource(R.drawable.ic_heart_red)
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_animation))
            } else {
                setImageResource(R.drawable.ic_heart_dark)
                clearAnimation()
            }
        }
    }
    
    override fun displayGuideline(guideline: Int) {
        textViewHelp?.text = if (isRecording()) getText(guideline)
        else getString(R.string.title_help)
    }
    
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonStart -> onButtonStartClicked()
        }
    }
    
    private fun onButtonStartClicked() {
        when {
            !hasCameraPermissions() -> requestCameraPermissions()
            isRecording() -> closeCamera()
            else -> openCamera()
        }
    }
    
    private fun nextToSaveHeartBeatFragment(progressPercent: Int, numberRate: Int) {
        if (progressPercent > PERCENT_NEXT_FRAGMENT)
            onLoadFragment.nextFragment(
                SaveHeartbeatFragment.newInstance(this,
                    numberRate,
                    System.currentTimeMillis()
                )
            )
    }
    
    private fun hasCameraPermissions() = context?.let {
        getCurrentCameraPermission(it) == PackageManager.PERMISSION_GRANTED
    } ?: false
    
    private fun getCurrentCameraPermission(context: Context) =
        ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
    
    override fun backFragment() {
        onLoadFragment.backFragment()
    }
    
    override fun onPause() {
        super.onPause()
        if (isRecording()) closeCamera()
    }
    
    companion object {
        const val CODE_PERMISSION_CAMERA = 200
        const val MEASUREMENT_TIME = 26000L
        const val PERCENT_NEXT_FRAGMENT = 97
        
        @JvmStatic
        fun newInstance(onLoadFragment: OnLoadFragment) = HeartbeatFragment(onLoadFragment)
    }
    
    interface OnLoadFragment {
        fun nextFragment(fragment: Fragment)
        fun backFragment()
    }
}
