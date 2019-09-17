package com.sun.heartrate.utils

import android.view.View
import java.text.DecimalFormat
import java.text.SimpleDateFormat

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.show() {
    this?.visibility = View.VISIBLE
}

fun View.OnClickListener.assignViews(vararg views: View?) {
    views.forEach { it?.setOnClickListener(this) }
}

fun Long.formatDate(): String = this.let {
    SimpleDateFormat("HH:mm:ss dd-MM-yyyy").format(it)
}

fun Int.formatDecimal(): String = this.let {
    DecimalFormat("#000").format(it)
}
