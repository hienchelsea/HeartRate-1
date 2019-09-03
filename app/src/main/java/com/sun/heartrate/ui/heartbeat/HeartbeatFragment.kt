package com.sun.heartrate.ui.heartbeat

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.sun.heartrate.R
import com.sun.heartrate.data.database.HeartDatabase
import com.sun.heartrate.data.repository.HeartRepository
import com.sun.heartrate.data.source.HeartLocalDataSource
import kotlinx.android.synthetic.main.fragment_heartbeat.*

class HeartbeatFragment : Fragment(), HeartbeatContract.View, View.OnClickListener {
    override fun getCameraManager(): CameraManager = activity?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    
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
    
    private val checkPermissions by lazy {
        context?.let {
            ActivityCompat.checkSelfPermission(
                it,
                Manifest.permission.CAMERA)
        }
    }
    
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
    
    private fun openCamera() {
        heartbeatPresenter.openCamera()
        upDateIuButton(heartbeatPresenter.checkCamera())
    }
    
    override fun closeCamera() {
        heartbeatPresenter.closeCamera()
        upDateIuButton(heartbeatPresenter.checkCamera())
    }
    
    private fun requestPermissions() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.CAMERA),
                CODE_PERMISSION_CAMERA
            )
        }
    }
    
    private fun upDateIuButton(isCheckCamera: Boolean) {
        if (isCheckCamera) {
            buttonStart.text = getText(R.string.label_pause)
        } else {
            buttonStart.text = getText(R.string.label_start)
        }
    }
    
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.buttonStart -> {
                if (heartbeatPresenter.checkCamera()) {
                    closeCamera()
                } else {
                    if (checkPermissions != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions()
                    } else {
                        openCamera()
                    }
                }
                
            }
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE_PERMISSION_CAMERA
            && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        }
    }
    
    override fun onPause() {
        super.onPause()
        closeCamera()
    }
    
    companion object {
        @JvmStatic
        fun newInstance() = HeartbeatFragment()
        
        const val CODE_PERMISSION_CAMERA = 200
    }
}
