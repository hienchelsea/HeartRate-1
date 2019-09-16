package com.sun.heartrate.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sun.heartrate.data.model.HeartModel

class HeartDatabase(
    context: Context?
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }
    
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE)
    }
    
    fun insertHeart(heartModel: HeartModel): Boolean {
        val db = writableDatabase
        val values = createContentValues(heartModel)
        val result = db.insert(
            TABLE_NAME,
            null,
            values
        ) != -1L
        db.close()
        return result
    }
    
    fun getAllHeart(): List<HeartModel> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        return getHeart(cursor)
    }
    
    fun getHeartByMonth(month: String): List<HeartModel> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_MONTH = ?",
            arrayOf(month),
            null,
            null,
            null
        )
        return getHeart(cursor)
    }
    
    fun getHeartByStatus(image: Int): List<HeartModel> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_IMAGE = ?",
            arrayOf(image.toString()),
            null,
            null,
            null
        )
        return getHeart(cursor)
    }
    
    private fun getHeart(cursor: Cursor): List<HeartModel> {
        val hearts = mutableListOf<HeartModel>()
        if (cursor.moveToFirst()) {
            do {
                hearts.add(HeartModel(cursor))
            } while (cursor.moveToNext())
        }
        close()
        return hearts
    }
    
    fun deleteHeart(heartModel: HeartModel): Boolean {
        val db = this.writableDatabase
        val result = db.delete(
            TABLE_NAME,
            "$COLUMN_ID=?",
            arrayOf(heartModel.id.toString())
        ) != -1
        db.close()
        return result
    }
    
    private fun createContentValues(heartModel: HeartModel) = ContentValues().apply {
        put(COLUMN_TITLE, heartModel.title)
        put(COLUMN_HEART_RATE, heartModel.heartRate)
        put(COLUMN_IMAGE, heartModel.image)
        put(COLUMN_MONTH, heartModel.monthYear)
        put(COLUMN_TIME, heartModel.time)
    }
    
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "heart.db"
        const val TABLE_NAME = "table_heart"
        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_HEART_RATE = "heart_rate"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_TIME = "time"
        const val COLUMN_MONTH = "month"
        
        const val CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME(
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_TITLE TEXT,
                $COLUMN_HEART_RATE INTEGER,
                $COLUMN_IMAGE INTEGER,
                $COLUMN_MONTH TEXT,
                $COLUMN_TIME INTEGER
            );
       """
        
        const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
    
}
