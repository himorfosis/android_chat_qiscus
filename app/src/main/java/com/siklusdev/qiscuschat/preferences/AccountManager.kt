package com.siklusdev.qiscuschat.preferences

import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.siklusdev.qiscuschat.model.data.AccountData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountManager @Inject constructor(
    private val preferences: Preferences
) {

    fun setToken(data: String) {
        preferences.token = data
    }

    fun getToken() = preferences.token

    fun setAccount(data: QiscusAccount) {
        preferences.token = data.token
        preferences.username = data.username
        preferences.email = data.email
        preferences.avatar = data.avatar
    }

    fun getProfileAccount(): AccountData {

        val email = preferences.email
        val username = preferences.username
        val token = preferences.token
        val avatar = preferences.avatar

        return AccountData(
            email = preferences.email,
            username =  preferences.username,
            avatar = preferences.avatar
        )
    }

    fun logout() {
        preferences.clear()
    }

}