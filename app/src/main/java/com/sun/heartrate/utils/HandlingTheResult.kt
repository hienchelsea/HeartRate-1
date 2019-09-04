package com.sun.heartrate.utils

import com.sun.heartrate.ui.heartbeat.HeartbeatPresenter

object HandlingTheResult {
    private val rollingAverage: IntArray by lazy {
        IntArray(HeartbeatPresenter.SIZE_AVERAGE_INDEX)
    }
    private val beatsList: IntArray by lazy {
        IntArray(HeartbeatPresenter.SIZE_AVERAGE_INDEX)
    }
    private var beats = 0
    private var check = 0
    private var startTime = 0L
    private var firstResults = 0
    private var beatsIndex = 0
    private var averageIndex = 0
    
    fun handleResultImage(currentRolling: Int): Int {
        beats = amplitudeVaries(currentRolling, beats)
        rollingAverage[averageIndex] = currentRolling
        averageIndex++
        if (averageIndex == HeartbeatPresenter.SIZE_AVERAGE_INDEX) {
            averageIndex = 0
        }
        val heartAvg = averageHeartRateCurrent()
        return if (heartAvg > 0) heartAvg else 0
    }
    
    private fun amplitudeVaries(currentRolling: Int, beats: Int): Int {
        var beatsCurrent = beats
        var averageCnt = 0
        var averageAvg = 0
        var currentRollingAverage = 0
        for (i in 0 until HeartbeatPresenter.SIZE_AVERAGE_INDEX) {
            if (rollingAverage[i] > 0) {
                averageAvg += rollingAverage[i]
                averageCnt++
            }
        }
        if (averageCnt > 0) {
            currentRollingAverage = averageAvg / averageCnt
        }
        val checkCurrent: Int
        if (currentRolling < currentRollingAverage) {
            checkCurrent = 1
            if (check != checkCurrent) {
                beatsCurrent++
            }
        } else {
            checkCurrent = 0
        }
        if (check != checkCurrent) {
            check = checkCurrent
        }
        return beatsCurrent
    }
    
    private fun averageHeartRateCurrent(): Int {
        val totalTimeInSecs = (System.currentTimeMillis() - startTime).toDouble() / 1000
        val timeWait = if (firstResults == 0) 10 else 5
        if (totalTimeInSecs >= timeWait) {
            val bps = (beats.toDouble() / totalTimeInSecs)
            val bmp: Int = (bps * 60).toInt()
            if (bmp < 30 || bmp > 180) {
                beats = 0
                startTime = System.currentTimeMillis()
                return 0
            } else {
                beatsList[beatsIndex] = bmp
                beatsIndex++
                if (beatsIndex == HeartbeatPresenter.SIZE_AVERAGE_INDEX) {
                    beatsIndex = 0
                }
                var beatsArrayAvg = 0
                var beatsArrayCnt = 0
                for (i in 0 until HeartbeatPresenter.SIZE_AVERAGE_INDEX) {
                    if (beatsList[i] > 0) {
                        beatsArrayAvg += beatsList[i]
                        beatsArrayCnt++
                    }
                }
                startTime = System.currentTimeMillis()
                beats = 0
                firstResults = 1
                return beatsArrayAvg / beatsArrayCnt
            }
        } else {
            return 0
        }
    }
    
}
