package com.siklusdev.qiscuschat.repo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.qiscus.sdk.chat.core.data.remote.QiscusApi
import com.siklusdev.qiscuschat.common.states.ActionLiveData
import com.siklusdev.qiscuschat.common.states.UiState
import com.siklusdev.qiscuschat.extensions.errorMesssage
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class ContactViewModel@ViewModelInject constructor(
): ViewModel()  {

    val contactState = ActionLiveData<UiState>()

    val contactResponse = MutableLiveData<List<QiscusAccount?>>()

    fun getContacts() {

        contactState.sendAction(UiState.Loading)

        QiscusApi.getInstance().getUsers(1, 100, "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { qiscusAccounts: List<QiscusAccount?>? ->
                    contactResponse.postValue(qiscusAccounts)
                    contactState.sendAction(UiState.Success)
                },
                { throwable: Throwable? ->
                    contactState.sendAction(UiState.Error(throwable?.errorMesssage ?: "Gagal"))
                }
            )
    }

}