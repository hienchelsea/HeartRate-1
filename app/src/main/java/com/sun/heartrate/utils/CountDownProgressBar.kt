package com.sun.heartrate.utils

import android.os.CountDownTimer

class CountDownProgressBar(
    duration: Long,
    private val onTicked: (millisUntilFinished: Long) -> Unit
) : CountDownTimer(duration, DEFAULT_INTERVAL) {
    override fun onFinish() {
        // no ops
    }
    
    override fun onTick(millisUntilFinished: Long) {
        onTicked(millisUntilFinished)
    }
    
    companion object {
        const val DEFAULT_INTERVAL = 500L
    }
}
