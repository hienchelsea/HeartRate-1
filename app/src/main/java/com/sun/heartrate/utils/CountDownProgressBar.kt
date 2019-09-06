package com.sun.heartrate.utils

import android.os.CountDownTimer

class CountDownProgressBar(
    duration: Long,
    private val onFinished: (millisUntilFinished: Long) -> Unit
) : CountDownTimer(duration, DEFAULT_INTERVAL) {
    override fun onFinish() {
        // no ops
    }
    
    override fun onTick(millisUntilFinished: Long) {
        onFinished(millisUntilFinished)
    }
    
    companion object {
        const val DEFAULT_INTERVAL = 500L
    }
}
