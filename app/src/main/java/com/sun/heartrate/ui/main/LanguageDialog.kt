package com.sun.heartrate.ui.main

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.Window
import com.sun.heartrate.R

class LanguageDialog {
    fun languageDialog(context: Context){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dailog_language)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.BOTTOM)
        
//        dialog.buttonDelete.setOnClickListener {
//           // deleteHeartDialogCallback.loadDeleteHeartDialogCallback(heartModel,check)
//            dialog.dismiss()
//        }
//
//        dialog.buttonCancel.setOnClickListener {
//            dialog.dismiss()
//        }
        
        dialog.show()
    }
    
    interface DeleteHeartDialogCallback {
        
        fun loadLanguageDialogCallback(language: String)
    }
}