package com.siklusdev.qiscuschat.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.siklusdev.qiscuschat.common.states.UiState
import com.siklusdev.qiscuschat.databinding.ActivityLoginBinding
import com.siklusdev.qiscuschat.extensions.toast
import com.siklusdev.qiscuschat.model.request.LoginRequest
import com.siklusdev.qiscuschat.module.BaseActivity
import com.siklusdev.qiscuschat.repo.AuthViewModel
import com.siklusdev.qiscuschat.ui.MainActivity
import com.siklusdev.qiscuschat.ui.dialog.DialogLoading
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val viewModel by viewModels<AuthViewModel>()

    lateinit var loading: DialogLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.logout()

        binding.submitBtn.onClick {

            val userId = binding.userId.text.toString()
            val username = binding.usernameEd.text.toString()
            val password = binding.passwordEd.text.toString()

            if (userId.isEmpty()) {
                toast("Harap isi user is")
            } else if (username.isEmpty()) {
                toast("Harap isi nama")
            } else if (password.isEmpty()) {
                toast("Harap isi password")
            } else {
                isLoading()
                viewModel.login(
                    LoginRequest(
                        user_id = userId,
                        username = username,
                        password = password
                    )
                )
            }

        }

        observe(viewModel.loginState) {
            when (it) {
                UiState.Success -> {
                    stopLoading()
                    startActivity(intentFor<MainActivity>())
                }
                is UiState.Error -> {
                    stopLoading()
                    toast("Gagal karena ${it.message}")
                }
            }
        }

    }

    private fun fetchLogin() {

//        QiscusCore.setUser("userId", "userKey")
//            .withUsername("username")
//            .withAvatarUrl("avatarUrl")
//            .withExtras(extras)
//            .save(new QiscusCore.SetUserListener() {
//                @Override
//                public void onSuccess(QiscusAccount qiscusAccount) {
//                    //on success
//                }
//                @Override
//                public void onError(Throwable throwable) {
//                    //on error
//                });

        QiscusCore.setUser("", "")
            .withUsername("")
            .withAvatarUrl("")
            .save(object : QiscusCore.SetUserListener {
                override fun onSuccess(qiscusAccount: QiscusAccount?) {

                }

                override fun onError(error: Throwable?) {

                }
            })

    }

    private fun isLoading() {
        loading = DialogLoading(this)
        loading.setCancelable(false)
        loading.show()
    }

    private fun stopLoading() {
        loading.dismiss()
    }

}