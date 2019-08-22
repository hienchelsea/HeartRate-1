package com.sun.heartrate.utils.animator

import android.os.CountDownTimer

class CountDownAnimation(
    duration: Long,
    private val onFinished: () -> Unit
) : CountDownTimer(duration, DEFAULT_INTERVAL) {
    
    override fun onTick(millisUntilFinished: Long) {
        // no ops
    }
    
    override fun onFinish() {
        onFinished()
    }
    
    companion object {
        const val DEFAULT_INTERVAL = 1000L
    }
}
