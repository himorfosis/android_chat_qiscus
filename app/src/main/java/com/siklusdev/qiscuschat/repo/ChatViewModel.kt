package com.siklusdev.qiscuschat.repo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.remote.QiscusApi
import com.siklusdev.qiscuschat.common.states.ActionLiveData
import com.siklusdev.qiscuschat.common.states.UiState
import com.siklusdev.qiscuschat.extensions.errorMesssage
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class ChatViewModel @ViewModelInject constructor(
): ViewModel() {

    val chatRoomState = ActionLiveData<UiState>()
    val createChatRoomState = ActionLiveData<UiState>()

    val chatRoomResponse = MutableLiveData<List<QiscusChatRoom?>>()

    fun getChatRooms() {

        chatRoomState.sendAction(UiState.Loading)

        QiscusApi.getInstance()
            .getAllChatRooms(true, false, true, 1, 50)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { chatRoomList: List<QiscusChatRoom?>? ->
                    chatRoomResponse.postValue(chatRoomList)
                }
            ) { throwable: Throwable? ->
                chatRoomState.sendAction(UiState.Error(throwable?.errorMesssage ?: "Gagal"))
            }

    }

    fun createChatRoom(userId: String) {

        createChatRoomState.sendAction(UiState.Loading)

        QiscusApi.getInstance().chatUser(userId, null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { chatRoom: QiscusChatRoom? ->

                }
            ) { throwable: Throwable? ->
                createChatRoomState.sendAction(UiState.Error(throwable?.errorMesssage ?: "Gagal"))
            }
    }

}