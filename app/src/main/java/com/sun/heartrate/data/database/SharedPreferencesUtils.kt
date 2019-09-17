package com.sun.heartrate.data.database

import android.content.Context

class SharedPreferencesUtils(private val context: Context?) {
    
    fun setString(key: String,value:String ){
        context?.getSharedPreferences(
            PREFERENCES_NAME, 0
        )?.apply {
            edit()?.apply {
                putString(key, value)
                apply()
            }
        }
    }
    
    fun getString(key: String):String= context?.getSharedPreferences(
        PREFERENCES_NAME, 0
    )?.getString(key, "en").toString()
    
    companion object {
        const val PREFERENCES_NAME = "PREFERENCES_NAME"
    }
}
