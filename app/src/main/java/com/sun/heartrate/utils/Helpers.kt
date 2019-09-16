package com.sun.heartrate.utils

import com.sun.heartrate.R

fun createProgressPercent(current: Int, max: Int) =
    current * 100 / max

fun getIdStatus(value: String): Int{
    if(value==Constant.GENERAL) return R.drawable.ic_general_red
    if(value==Constant.BEFORE_TRAINING) return R.drawable.ic_before_training_red
    if(value==Constant.AFTER_TRAINING) return R.drawable.ic_after_training_red
    if(value==Constant.HEAVY_TRAINING) return R.drawable.ic_heavy_training_red
    if(value==Constant.RESTED) return R.drawable.ic_rested_red
    return 0
}
