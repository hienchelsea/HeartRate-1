package com.sun.heartrate.utils

import com.sun.heartrate.R
import com.sun.heartrate.data.model.HeartModel

fun createProgressPercent(current: Int, max: Int) =
    current * 100 / max

fun getStatusId(value: String): Int =
    when (value) {
        Constant.SELECT_IMAGE_GENERAL -> R.drawable.ic_general_red
        Constant.SELECT_IMAGE_BEFORE_TRAINING -> R.drawable.ic_before_training_red
        Constant.SELECT_IMAGE_AFTER_TRAINING -> R.drawable.ic_after_training_red
        Constant.SELECT_IMAGE_HEAVY_TRAINING -> R.drawable.ic_heavy_training_red
        Constant.SELECT_IMAGE_RESTED -> R.drawable.ic_rested_red
        else -> 0
    }

fun getHeartsMonths(hearts: List<HeartModel>): List<String> {
    val heartsMonths = mutableListOf<String>().apply {
        add(hearts[0].monthYear)
    }
    
    for (i in 1 until hearts.size) {
        if (hearts[i].monthYear != hearts[i - 1].monthYear) {
            heartsMonths.add(hearts[i].monthYear)
        }
    }
    return heartsMonths
}
