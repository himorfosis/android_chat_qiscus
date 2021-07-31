package com.siklusdev.qiscuschat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.siklusdev.qiscuschat.R
import com.siklusdev.qiscuschat.ui.auth.LoginActivity
import kotlinx.coroutines.*
import org.jetbrains.anko.intentFor

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        GlobalScope.launch(Dispatchers.IO) {
            delay(2000)
            withContext(Dispatchers.Main) {
                startActivity(intentFor<LoginActivity>())
            }
        }

    }
}