package com.siklusdev.qiscuschat.preferences

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

    fun setProfileAccount(data: AccountData) {
        preferences.email = data.email
        preferences.nama = data.nama
        preferences.phone = data.phone
        preferences.image = data.image
    }

    fun getProfileAccount(): AccountData {

        val email = preferences.email
        val nama = preferences.nama
        val phone = preferences.phone
        val image = preferences.image

        return AccountData(
            email = preferences.email,
            nama =  preferences.nama,
            phone = preferences.phone,
            image = preferences.image
        )
    }

}