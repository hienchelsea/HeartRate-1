package com.sun.heartrate.ui.heartbeat

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.sun.heartrate.R
import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.repository.HeartRepository
import com.sun.heartrate.data.source.HeartLocalDataSource
import com.sun.heartrate.ui.heartbeat.callback.OnImageAvailableListener
import com.sun.heartrate.ui.heartbeat.callback.SessionStateCallback
import com.sun.heartrate.ui.heartbeat.callback.StateCallback
import kotlinx.android.synthetic.main.fragment_heartbeat.*

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.KITKAT)
class HeartbeatFragment : Fragment(), HeartbeatContract.View, View.OnClickListener {
    
    private val heartbeatPresenter: HeartbeatContract.Presenter by lazy {
        HeartbeatPresenter(this, heartRepository)
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
    
    private val imageReader: ImageReader by lazy {
        ImageReader.newInstance(
            WIDTH_IMAGE_READER,
            HEIGHT_IMAGE_READER,
            ImageFormat.YUV_420_888,
            MAXIMUM_NUMBER_OF_IMAGE
        )
    }
    private val backgroundThread: HandlerThread by lazy {
        HandlerThread("Camera Background").apply {
            start()
        }
    }
    private val backgroundHandler: Handler by lazy {
        Handler(backgroundThread.looper)
    }
    
    private var cameraDevice: CameraDevice? = null
    private var timeStart = 0L
    
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
    
    private fun initView() {
        buttonStart?.setOnClickListener(this)
    }
    
    @SuppressLint("MissingPermission")
    fun openCamera() {
        try {
            val manager: CameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId = manager.cameraIdList[0]
            manager.run {
                openCamera(cameraId,
                    StateCallback { camera: CameraDevice? ->
                        camera?.let { cameraPreview(it) }
                    },
                    null
                )
                timeStart = System.currentTimeMillis()
            }
            
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        
    }
    
    private fun cameraPreview(cameraDevice: CameraDevice) {
        createCameraPreview(cameraDevice)
    }
    
    private fun createCameraPreview(cameraDevice: CameraDevice) {
        try {
            this.cameraDevice = cameraDevice
            val surfaceArrayList = mutableListOf<Surface>().apply {
                add(imageReader.surface)
            }
            imageReader.setOnImageAvailableListener(
                OnImageAvailableListener(heartRepository) { values: Int ->
                    loadDataImage(values)
                },
                null
            )
            cameraDevice.createCaptureSession(
                surfaceArrayList,
                SessionStateCallback(cameraDevice, imageReader)
                { cameraCaptureSession: CameraCaptureSession,
                  builder: CaptureRequest.Builder ->
                    updatePreview(cameraCaptureSession, builder)
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        
    }
    
    private fun updatePreview(
        cameraCaptureSessions: CameraCaptureSession,
        captureRequestBuilder: CaptureRequest.Builder
    ) {
        try {
            cameraCaptureSessions.setRepeatingRequest(
                captureRequestBuilder.build(),
                null,
                backgroundHandler
            )
            
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        
    }
    
    override fun closeCamera() {
        cameraDevice?.close()
        cameraDevice = null
        backgroundThread.quitSafely()
    }
    
    private fun loadDataImage(value: Int) {
        heartbeatPresenter.calculateHeartRate(value, timeStart)
    }
    
    private fun requestPermissions() {
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.CAMERA)
            } != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.CAMERA),
                    CODE_PERMISSION_CAMERA
                )
            }
        } else {
            openCamera()
        }
    }
    
    private fun upDateIuButton() {
        if (checkCamera()) {
            buttonStart.text = getText(R.string.label_start)
            closeCamera()
        } else {
            requestPermissions()
            buttonStart.text = getText(R.string.label_pause)
        }
    }
    
    private fun checkCamera(): Boolean {
        if (cameraDevice == null) {
            return false
        }
        return true
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CODE_PERMISSION_CAMERA -> {
                if ((grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    openCamera()
                }
            }
        }
    }
    
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonStart -> {
                upDateIuButton()
            }
        }
    }
    
    override fun onPause() {
        super.onPause()
        cameraDevice?.let {
            upDateIuButton()
        }
    }
    
    companion object {
        @JvmStatic
        fun newInstance() = HeartbeatFragment()
        
        const val CODE_PERMISSION_CAMERA = 200
        const val WIDTH_IMAGE_READER = 960
        const val HEIGHT_IMAGE_READER = 540
        const val MAXIMUM_NUMBER_OF_IMAGE = 3
    }
}
