package com.sun.heartrate.ui.main

import com.sun.heartrate.data.database.SharedPreferencesUtils
import com.sun.heartrate.data.repository.HeartRepository

class MainPresenter(
    private val heartRepository: HeartRepository,
    private val sharedPreferencesUtils: SharedPreferencesUtils,
    private val view: MainContract.View
) : MainContract.Presenter {
    override fun setLanguage(key: String, value: String) {
        heartRepository.setStringLanguage(sharedPreferencesUtils, key, value)
    }
    
    override fun getLanguage(
        key: String
    ): String = heartRepository.getStringLanguage(sharedPreferencesUtils, key)
    
    override fun setNotification(key: String, value: Boolean){
        heartRepository.setBooleanNotification(sharedPreferencesUtils,key,value)
    }
    
    override fun getNotification(
        key: String
    ): Boolean? = heartRepository.getBooleanNotification(sharedPreferencesUtils, key)
    
}
