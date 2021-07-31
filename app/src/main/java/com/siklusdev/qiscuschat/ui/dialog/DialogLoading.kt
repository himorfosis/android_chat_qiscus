package com.siklusdev.qiscuschat.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.siklusdev.qiscuschat.R
import com.siklusdev.qiscuschat.databinding.DialogLoadingBinding

class DialogLoading(context: Context): Dialog(context) {

    init {

        val binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.attributes?.windowAnimations = R.style.DialogAnimation
        setContentView(binding.root)
        this.setCancelable(false)

    }

}