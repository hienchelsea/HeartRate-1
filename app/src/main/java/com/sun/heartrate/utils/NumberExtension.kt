package com.sun.heartrate.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat

fun Long.formatDate(): String = this.let {
    SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(it)
}

fun Long.formatMonth(): String = this.let {
    SimpleDateFormat("MM-yyyy").format(it)
}

fun Int.formatDecimal(): String = this.let {
    DecimalFormat("#000").format(it)
}