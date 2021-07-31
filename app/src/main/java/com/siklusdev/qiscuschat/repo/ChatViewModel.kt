package com.siklusdev.qiscuschat.repo

import android.util.Log
import androidx.core.util.Pair
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.qiscus.sdk.chat.core.data.model.QiscusComment
import com.qiscus.sdk.chat.core.data.remote.QiscusApi
import com.siklusdev.qiscuschat.common.states.ActionLiveData
import com.siklusdev.qiscuschat.common.states.UiState
import com.siklusdev.qiscuschat.extensions.errorMesssage
import com.siklusdev.qiscuschat.preferences.AccountManager
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class ChatViewModel @ViewModelInject constructor(
    private val accountManager: AccountManager
) : ViewModel() {

    val chatRoomState = ActionLiveData<UiState>()
    val chatMessageState = ActionLiveData<UiState>()
    val createChatRoomState = ActionLiveData<UiState>()

    val chatRoomResponse = MutableLiveData<List<QiscusChatRoom?>>()
    val chatMessageResponse = MutableLiveData<List<QiscusComment>>()
    val createChatResponse = MutableLiveData<QiscusChatRoom>()

    // data
    private lateinit var room: QiscusChatRoom

    fun startChat(qiscusChatRoom: QiscusChatRoom?) {
        this.room = qiscusChatRoom!!
    }

    fun setMessageLive(qiscusComment: QiscusComment) {
        val listData: MutableList<QiscusComment> = ArrayList()
        listData.add(qiscusComment)
        chatMessageResponse.postValue(listData)
    }

    fun sendMessage(message: String) {

        isLog("sendMessage")

        val qiscusMessage = QiscusComment.generateMessage(room.id, message)

        QiscusApi.getInstance().sendMessage(qiscusMessage)
            .subscribeOn(Schedulers.io()) // need to run this task on IO thread
            .observeOn(AndroidSchedulers.mainThread()) // deliver result on main thread or UI thread
            .subscribe(
                { qiscusChatRoom: QiscusComment? ->

                }
            ) { throwable: Throwable? ->
                isLog("message: ${throwable?.errorMesssage}")
            }
    }

    fun getChatAllRooms() {

        isLog("getChatRooms")

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
                isLog("message: ${throwable?.errorMesssage}")
            }

    }

    fun loadMessage() {

        isLog("loadMessage")
        chatMessageState.sendAction(UiState.Loading)

        QiscusApi.getInstance().getChatRoomWithMessages(room.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ chatRoomListPair: Pair<QiscusChatRoom, List<QiscusComment>> ->
                // on success getting chat room
                val qiscusChatRoom = chatRoomListPair.first
                // on success getting messages
//                val messages = chatRoomListPair.second

                val messages = chatRoomListPair.second.sortedBy { it.time }

                chatMessageResponse.postValue(messages)
                isLog("success : ")

            }) { throwable: Throwable ->
                chatMessageState.sendAction(UiState.Error(throwable?.errorMesssage))
                isLog("error: ${throwable.errorMesssage}")
            }

    }

    fun createChatRoom(userId: String) {

        isLog("createChatRoom")

        createChatRoomState.sendAction(UiState.Loading)

        QiscusApi.getInstance().chatUser(userId, null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { chatRoom: QiscusChatRoom? ->
                    createChatResponse.postValue(chatRoom)
                    createChatRoomState.sendAction(UiState.Success)
                }
            ) { throwable: Throwable? ->
                createChatRoomState.sendAction(UiState.Error(throwable?.errorMesssage ?: "Gagal"))
                isLog("error: ${throwable?.errorMesssage}")
            }
    }

    private fun isLog(msg: String) {
        Log.e("Chat VM", msg)
    }

}

