package com.siklusdev.qiscuschat.repo

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qiscus.sdk.chat.core.QiscusCore
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.siklusdev.qiscuschat.common.states.ActionLiveData
import com.siklusdev.qiscuschat.common.states.UiState
import com.siklusdev.qiscuschat.model.data.AccountData
import com.siklusdev.qiscuschat.model.request.LoginRequest
import com.siklusdev.qiscuschat.preferences.AccountManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthViewModel @ViewModelInject constructor(
    private val accountManager: AccountManager
) : ViewModel() {

    val loginResponse = MutableLiveData<QiscusAccount?>()

    val loginState = ActionLiveData<UiState>()

    val isLoggedIn: Boolean
        get() = accountManager.getToken().isNotBlank()

    val getProfileUser: AccountData
        get() = accountManager.getProfileAccount()

    fun login(data: LoginRequest) {

        viewModelScope.launch {
            loginState.sendAction(UiState.Loading)

            QiscusCore.setUser(data.user_id, data.password)
                .withUsername(data.username)
                .save(object : QiscusCore.SetUserListener {
                    override fun onSuccess(account: QiscusAccount?) {
                        accountManager.setAccount(account!!)
                        loginState.sendAction(UiState.Success)
                    }

                    override fun onError(error: Throwable?) {
                        isLog("error: ${error?.message}")
                        loginState.sendAction(UiState.Error(error?.message ?: "Gagal"))
                    }
                })
        }

    }

    fun logout() {
        GlobalScope.launch {
            accountManager.logout()
        }
    }

    private fun isLog(msg: String) {
        Log.e("Auth", msg)
    }

}