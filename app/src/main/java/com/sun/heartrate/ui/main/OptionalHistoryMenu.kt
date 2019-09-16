package com.sun.heartrate.ui.main

import android.content.Context
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import com.sun.heartrate.R
import com.sun.heartrate.utils.Constant

class OptionalHistoryMenu(private val menuOptionCallback:MenuOptionCallback) : MenuBuilder.Callback {
    
    fun optionalHistoryMenu(context: Context, imageView: ImageView) {
        val menuBuilder = MenuBuilder(context)
        MenuInflater(context).apply {
            inflate(R.menu.menu_optional_history, menuBuilder)
        }
        val optionsMenu = MenuPopupHelper(context, menuBuilder, imageView)
        optionsMenu.setForceShowIcon(false)
        optionsMenu.show()
        menuBuilder.setCallback(this)
    }
    
    override fun onMenuModeChange(menu: MenuBuilder?) {
    }
    
    override fun onMenuItemSelected(menu: MenuBuilder?, item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.itemGeneral -> {
                menuOptionCallback.loadMenuOptionCallback(Constant.GENERAL)
                return true
            }
            R.id.itemBeforeTraining -> {
                menuOptionCallback.loadMenuOptionCallback(Constant.BEFORE_TRAINING)
                return true
            }
            R.id.itemAfterTraining -> {
                menuOptionCallback.loadMenuOptionCallback(Constant.AFTER_TRAINING)
                return true
            }
            R.id.itemHeavyTraining -> {
                menuOptionCallback.loadMenuOptionCallback(Constant.HEAVY_TRAINING)
                return true
            }
            R.id.itemRested -> {
                menuOptionCallback.loadMenuOptionCallback(Constant.RESTED)
                return true
            }
            R.id.itemShowAll -> {
                menuOptionCallback.loadMenuOptionCallback(Constant.SHOW_ALL)
                return true
            }
            else -> return false
        }
    }
    interface MenuOptionCallback{
        fun loadMenuOptionCallback(value: String)
    }
}