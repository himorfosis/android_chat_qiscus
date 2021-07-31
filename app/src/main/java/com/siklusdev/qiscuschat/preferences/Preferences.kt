package com.siklusdev.qiscuschat.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(
    private val preferences: SharedPreferences
) {

    var token: String by PreferenceData(
        preferences,
        "token",
        ""
    )

    // ACCOUNT
    var phone: String by PreferenceData(
        preferences,
        "phone",
        ""
    )
    var email: String by PreferenceData(
        preferences,
        "email",
        ""
    )
    var nama: String by PreferenceData(
        preferences,
        "nama",
        ""
    )

    var image: String by PreferenceData(
        preferences,
        "image",
        ""
    )


}