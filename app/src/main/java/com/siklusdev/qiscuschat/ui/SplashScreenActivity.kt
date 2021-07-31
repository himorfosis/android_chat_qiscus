package com.siklusdev.qiscuschat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.siklusdev.qiscuschat.R
import com.siklusdev.qiscuschat.databinding.ActivityMainBinding
import com.siklusdev.qiscuschat.databinding.ActivitySplashScreenBinding
import com.siklusdev.qiscuschat.repo.AuthViewModel
import com.siklusdev.qiscuschat.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.jetbrains.anko.intentFor

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch(Dispatchers.IO) {
            delay(2000)
            withContext(Dispatchers.Main) {
                try {
                    val isLogin = viewModel.isLoggedIn
                    if (isLogin) {
                        startActivity(intentFor<MainActivity>())
                        finish()
                    } else {
                        startActivity(intentFor<LoginActivity>())
                        finish()
                    }
                } catch (error: Exception) {
                    startActivity(intentFor<LoginActivity>())
                    finish()
                }
            }
        }

    }
}