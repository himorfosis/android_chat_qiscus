package com.siklusdev.qiscuschat.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.siklusdev.qiscuschat.databinding.FragmentProfileBinding
import com.siklusdev.qiscuschat.extensions.loadProfileUrl
import com.siklusdev.qiscuschat.module.BaseFragmentBinding
import com.siklusdev.qiscuschat.repo.AuthViewModel
import com.siklusdev.qiscuschat.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor

@AndroidEntryPoint
class ProfileFragment :
    BaseFragmentBinding<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getProfileUser.let {

            binding.nameTv.text = it.username
            binding.emailTv.text = it.email
            binding.profileImg.loadProfileUrl(it.avatar)

            isLog("avatar : ${it.avatar}")

        }

        binding.logoutBtn.onClick {
            viewModel.logout()
            startActivity(intentFor<LoginActivity>())
            activity?.finish()
        }

    }

    private fun isLog(msg: String) {
        Log.e("home", msg)
    }

}