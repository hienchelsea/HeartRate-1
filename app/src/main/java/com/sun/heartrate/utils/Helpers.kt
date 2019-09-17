package com.sun.heartrate.utils

fun createProgressPercent(current: Int, max: Int) =
    current * 100 / max

fun createProgressBarSaveHeartPercent(current: Int, min: Int, max: Int) =
    (current - min) * 100 / (max - min)
