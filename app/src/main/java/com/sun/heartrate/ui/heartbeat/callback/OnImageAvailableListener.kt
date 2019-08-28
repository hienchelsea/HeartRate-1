package com.sun.heartrate.ui.heartbeat.callback

import android.annotation.TargetApi
import android.media.Image
import android.media.ImageReader
import android.os.Build
import com.sun.heartrate.data.repository.HeartRepository

@TargetApi(Build.VERSION_CODES.KITKAT)
class OnImageAvailableListener(
    private val heartRepository: HeartRepository,
    private val onLoadImage: (values: Int) -> Unit
) : ImageReader.OnImageAvailableListener {
    
    override fun onImageAvailable(reade: ImageReader) {
        try {
            val image: Image = reade.acquireLatestImage()
            onLoadImage(heartRepository.imageExtensions(image))
            image.close()
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
    }
}
