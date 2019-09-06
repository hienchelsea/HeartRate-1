package com.sun.heartrate.ui.heartbeat.callback

import android.media.Image
import android.media.ImageReader
import com.sun.heartrate.utils.ImageProcessingHelper
import java.nio.ByteBuffer

class OnImageAvailableListener(
    private val onLoadImage: (values: ByteArray, widthImage: Int, heightImage: Int) -> Unit
) : ImageReader.OnImageAvailableListener {
    
    override fun onImageAvailable(reade: ImageReader) {
        try {
            val image: Image = reade.acquireLatestImage()
            val buffer: ByteBuffer = ImageProcessingHelper.imageToByteBuffer(image)
            val arr = buffer.array()
            onLoadImage(arr, image.width, image.height)
            image.close()
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
    }
}
