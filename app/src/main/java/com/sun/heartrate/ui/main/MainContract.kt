package com.sun.heartrate.ui.main

interface MainContract {
    interface View
    
    interface Presenter {
        fun setLanguage(key: String, value: String)
        fun getLanguage(key: String): String
    }
}
