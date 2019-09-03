package com.sun.heartrate.utils

import android.graphics.ImageFormat
import android.media.Image
import java.nio.ByteBuffer

object ImageProcessingHelper {
    private fun decodeYUV420SPtoRedAvg(yuv420sp: ByteArray, width: Int, height: Int): Int {
        val frameSize = width * height
        val sum = decodeYUV420SPtoRedSum(yuv420sp, width, height)
        return (sum / frameSize)
    }
    
    private fun decodeYUV420SPtoRedSum(yuv420sp: ByteArray, width: Int, height: Int): Int {
        val frameSize = width * height
        var sum = 0
        var j = 0
        var yp = 0
        while (j < height) {
            var uvp = frameSize + (j shr 1) * width
            var u = 0
            var v = 0
            var i = 0
            while (i < width) {
                var y = (0xff and yuv420sp[yp].toInt()) - 16
                if (y < 0) y = 0
                if (i and 1 == 0) {
                    v = (0xff and yuv420sp[uvp++].toInt()) - 128
                    u = (0xff and yuv420sp[uvp++].toInt()) - 128
                }
                val y1192 = 1192 * y
                var r = y1192 + 1634 * v
                var g = y1192 - 833 * v - 400 * u
                var b = y1192 + 2066 * u
                
                if (r < 0)
                    r = 0
                else if (r > 262143) r = 262143
                if (g < 0)
                    g = 0
                else if (g > 262143) g = 262143
                if (b < 0)
                    b = 0
                else if (b > 262143) b = 262143
                val pixel = -0x1000000 or (r shl 6 and 0xff0000) or (g shr 2 and 0xff00) or (b shr 10 and 0xff)
                val red = pixel shr 16 and 0xff
                sum += red
                i++
                yp++
            }
            j++
        }
        return sum
    }
    
    fun imageToByteBuffer(image: Image): ByteBuffer {
        val crop = image.cropRect
        val width = crop.width()
        val height = crop.height()
        
        val planes = image.planes
        val rowData = ByteArray(planes[0].rowStride)
        val bufferSize = width * height * ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888) / 8
        val output = ByteBuffer.allocateDirect(bufferSize)
        
        var channelOffset = 0
        var outputStride = 0
        
        for (planeIndex in 0..2) {
            when (planeIndex) {
                0 -> {
                    channelOffset = 0
                    outputStride = 1
                }
                1 -> {
                    channelOffset = width * height + 1
                    outputStride = 2
                }
                2 -> {
                    channelOffset = width * height
                    outputStride = 2
                }
            }
            
            val buffer = planes[planeIndex].buffer
            val rowStride = planes[planeIndex].rowStride
            val pixelStride = planes[planeIndex].pixelStride
            
            val shift = if (planeIndex == 0) 0 else 1
            val widthShifted = width shr shift
            val heightShifted = height shr shift
            
            buffer.position(
                rowStride
                    * (crop.top shr shift)
                    + pixelStride
                    * (crop.left shr shift)
            )
            
            for (row in 0 until heightShifted) {
                val length: Int
                
                if (pixelStride == 1 && outputStride == 1) {
                    length = widthShifted
                    buffer.get(output.array(), channelOffset, length)
                    channelOffset += length
                } else {
                    length = (widthShifted - 1) * pixelStride + 1
                    buffer.get(rowData, 0, length)
                    
                    for (col in 0 until widthShifted) {
                        output.array()[channelOffset] = rowData[col * pixelStride]
                        channelOffset += outputStride
                    }
                }
                
                if (row < heightShifted - 1) {
                    buffer.position(buffer.position() + rowStride - length)
                }
            }
        }
        return output
    }
    
    fun imageConversionProcessing(value: ByteArray, widthImage: Int, heightImage: Int): Int {
        return decodeYUV420SPtoRedAvg(
            value,
            widthImage,
            heightImage
        )
    }
}
