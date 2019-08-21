package com.sun.heartrate.data.model


import android.database.Cursor
import com.sun.heartrate.data.database.HeartDatabase

data class HeartModel(
    val id: Int,
    val title: String,
    val heartRate: Int,
    val image: Int,
    val monthYear: String,
    val time: Long
) {
    constructor(cursor: Cursor) : this(
        cursor.getString(cursor.getColumnIndex(HeartDatabase.COLUMN_ID)).toInt(),
        cursor.getString(cursor.getColumnIndex(HeartDatabase.COLUMN_TITLE)),
        cursor.getString(cursor.getColumnIndex(HeartDatabase.COLUMN_HEART_RATE)).toInt(),
        cursor.getString(cursor.getColumnIndex(HeartDatabase.COLUMN_IMAGE)).toInt(),
        cursor.getString(cursor.getColumnIndex(HeartDatabase.COLUMN_MONTH)),
        cursor.getString(cursor.getColumnIndex(HeartDatabase.COLUMN_TIME)).toLong()
    )
}
