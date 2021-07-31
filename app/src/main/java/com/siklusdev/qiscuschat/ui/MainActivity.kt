package com.siklusdev.qiscuschat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.siklusdev.qiscuschat.R
import com.siklusdev.qiscuschat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_main_fragment)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            val id = destination.id
            val hideToolbar = id == R.id.nav_home

            binding.toolbar.isGone = hideToolbar

        }

    }
}