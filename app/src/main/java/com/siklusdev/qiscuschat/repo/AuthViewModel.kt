package com.siklusdev.qiscuschat.repo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.siklusdev.qiscuschat.common.states.ActionLiveData
import com.siklusdev.qiscuschat.common.states.UiState
import com.siklusdev.qiscuschat.model.request.LoginRequest
import com.siklusdev.qiscuschat.network.services.AuthServices
import com.siklusdev.qiscuschat.preferences.AccountManager
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val services: AuthServices,
    private val accountManager: AccountManager
): ViewModel() {

    val loginResponse = MutableLiveData<QiscusAccount?>()

    val loginState = ActionLiveData<UiState>()

    fun login(data: LoginRequest) {

        loginState.sendAction(UiState.Loading)

        QiscusCore.setUser(data.user_id, data.password)
            .withUsername(data.username)
            .save(object : QiscusCore.SetUserListener {
                override fun onSuccess(qiscusAccount: QiscusAccount?) {
                    loginState.sendAction(UiState.Loading)
                }

                override fun onError(error: Throwable?) {
                    loginState.sendAction(UiState.Error(error?.message ?: "Gagal"))
                }
            })

//        viewModelScope.launch {
//            try {
//
//                // loadingState.sendAction(UiState.Success)
//            } catch (error: Exception) {
//                // loadingState.sendAction(UiState.Error(error.message!!))
//            }
//        }
    }

    fun register() {
        // loadingState.sendAction(UiState.Loading)
        viewModelScope.launch {
            try {

                // loadingState.sendAction(UiState.Success)
            } catch (error: Exception) {
                // loadingState.sendAction(UiState.Error(error.message!!))
            }
        }
    }


}