package com.siklusdev.qiscuschat.app

import android.app.Application
import com.bumptech.glide.Glide
import com.qiscus.sdk.chat.core.QiscusCore
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class AndroidApp: Application() {

    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(applicationContext).onTrimMemory(Glide.TRIM_MEMORY_RUNNING_LOW)
    }

    override fun onCreate() {
        super.onCreate()
        QiscusCore.setup(this, "sdksample")
    }
}