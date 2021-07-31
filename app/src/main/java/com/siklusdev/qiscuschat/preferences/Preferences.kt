package com.siklusdev.qiscuschat.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(
    private val preferences: SharedPreferences
) {

    var token: String by PreferenceData(preferences, "token", "")
    var username: String by PreferenceData(preferences, "username", "")
    var avatar: String by PreferenceData(preferences, "avatar", "")
    var email: String by PreferenceData(preferences, "email", "")

    fun clear() {
        preferences.edit().clear().apply()
    }


}
