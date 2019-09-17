package com.sun.heartrate.ui.history

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import com.sun.heartrate.R
import com.sun.heartrate.data.model.HeartModel
import kotlinx.android.synthetic.main.dailog_delete_note.*

class DeleteHeartDialog(
    private val deleteHeartDialogCallback:DeleteHeartDialogCallback
) {
    fun deleteHeartDialog(context: Context,heartModel: HeartModel,check:Int){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dailog_delete_note)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        
        dialog.buttonDelete.setOnClickListener {
            deleteHeartDialogCallback.loadDeleteHeartDialogCallback(heartModel,check)
            dialog.dismiss()
        }
    
        dialog.buttonCancel.setOnClickListener {
            dialog.dismiss()
        }
    
        dialog.show()
    }
    interface DeleteHeartDialogCallback{
        fun loadDeleteHeartDialogCallback(heartModel: HeartModel,check:Int)
    }
}
