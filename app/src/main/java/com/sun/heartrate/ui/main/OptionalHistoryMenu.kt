package com.sun.heartrate.ui.main

import android.content.Context
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import com.sun.heartrate.R
import com.sun.heartrate.utils.Constant

class OptionalHistoryMenu(
    private val menuOptionCallback: MenuOptionCallback
) : MenuBuilder.Callback {
    
    fun optionalHistoryMenu(context: Context, imageView: ImageView?) {
        val menuBuilder = MenuBuilder(context)
        MenuInflater(context).apply {
            inflate(R.menu.menu_optional_history, menuBuilder)
        }
        val optionsMenu = imageView?.let {
            MenuPopupHelper(
                context,
                menuBuilder,
                it
            )
        }
        
        optionsMenu?.apply {
            setForceShowIcon(false)
            show()
        }
        menuBuilder.setCallback(this)
    }
    
    override fun onMenuModeChange(menu: MenuBuilder?) {
    }
    
    override fun onMenuItemSelected(menu: MenuBuilder?, item: MenuItem?): Boolean {
        val valueName = when (item?.itemId) {
            R.id.itemGeneral -> Constant.SELECT_IMAGE_GENERAL
            R.id.itemBeforeTraining -> Constant.SELECT_IMAGE_BEFORE_TRAINING
            R.id.itemAfterTraining -> Constant.SELECT_IMAGE_AFTER_TRAINING
            R.id.itemHeavyTraining -> Constant.SELECT_IMAGE_HEAVY_TRAINING
            R.id.itemRested -> Constant.SELECT_IMAGE_RESTED
            R.id.itemShowAll -> Constant.SHOW_ALL
            else -> ""
        }
        menuOptionCallback.loadMenuOptionCallback(valueName)
        return true
    }
    
    interface MenuOptionCallback {
        fun loadMenuOptionCallback(value: String)
    }
}
