package com.sun.heartrate.data.database

import android.content.Context

class SharedPreferencesUtils(private val context: Context?) {
    
    fun setString(key: String, value: String) {
        context?.getSharedPreferences(
            PREFERENCES_NAME, 0
        )?.apply {
            edit()?.apply {
                putString(key, value)
                apply()
            }
        }
    }
    
    fun setBoolean(key: String, value: Boolean) {
        context?.getSharedPreferences(
            PREFERENCES_NAME, 0
        )?.apply {
            edit()?.apply {
                putBoolean(key, value)
                apply()
            }
        }
    }
    
    fun getString(key: String): String = context?.getSharedPreferences(
        PREFERENCES_NAME, 0
    )?.getString(key, "en").toString()
    
    fun getBoolean(key: String): Boolean? = context?.getSharedPreferences(
        PREFERENCES_NAME, 0
    )?.getBoolean(key, false)
    
    companion object {
        const val PREFERENCES_NAME = "PREFERENCES_NAME"
    }
}
