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
    context: Context,
    private val deleteHeartDialogCallback: DeleteHeartDialogCallback,
    private val heartModel: HeartModel,
    private val check: Int
) : Dialog(context) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dailog_delete_note)
        setCanceledOnTouchOutside(true)
        
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)
            setGravity(Gravity.CENTER)
        }
        initListener()
    }
    
    private fun initListener() {
        buttonDelete.setOnClickListener {
            deleteHeartDialogCallback.loadDeleteHeartDialogCallback(heartModel, check)
            dismiss()
        }
        
        buttonCancel.setOnClickListener {
            dismiss()
        }
    }
    
    interface DeleteHeartDialogCallback {
        fun loadDeleteHeartDialogCallback(heartModel: HeartModel, check: Int)
    }
}
