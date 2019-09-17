package com.sun.heartrate.ui.main

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import com.sun.heartrate.R
import kotlinx.android.synthetic.main.dailog_language.*

class LanguageDialog(
    private val languageDialogCallback: LanguageDialogCallback
) {
    fun languageDialog(context: Context){
        var language="en"
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dailog_language)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.BOTTOM)
        
        dialog.buttonOk.setOnClickListener{
            if(dialog.radioButtonVietnamese.isChecked) language="vi"
            if(dialog.radioButtonEnglish.isChecked) language="en"
            if(dialog.radioButtonJapanese.isChecked) language="ja"
            languageDialogCallback.onChangeLanguage(language)
            dialog.dismiss()
        }
        dialog.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
    
    interface LanguageDialogCallback {
        fun onChangeLanguage(language: String)
    }
}
