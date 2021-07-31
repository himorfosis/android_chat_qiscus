package com.siklusdev.qiscuschat.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.qiscus.sdk.chat.core.data.remote.QiscusPusherApi
import com.qiscus.sdk.chat.core.event.QiscusChatRoomEvent
import com.qiscus.sdk.chat.core.event.QiscusCommentReceivedEvent
import com.qiscus.sdk.chat.core.event.QiscusUserStatusEvent
import com.siklusdev.qiscuschat.databinding.FragmentChatRoomBinding
import com.siklusdev.qiscuschat.extensions.loadProfileUrl
import com.siklusdev.qiscuschat.module.BaseFragmentBinding
import com.siklusdev.qiscuschat.repo.AuthViewModel
import com.siklusdev.qiscuschat.repo.ChatViewModel
import com.siklusdev.qiscuschat.ui.chat.adapter.ChatAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.sdk27.coroutines.onClick


@AndroidEntryPoint
class ChatRoomFragment :
    BaseFragmentBinding<FragmentChatRoomBinding>(FragmentChatRoomBinding::inflate) {

    private val viewModel by viewModels<ChatViewModel>()
    private val viewModelAuth by viewModels<AuthViewModel>()
    private val args by navArgs<ChatRoomFragmentArgs>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ChatAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startChat(args.chatRoom)
        viewModel.loadMessage()

        QiscusPusherApi.getInstance().subscribeChatRoom(args.chatRoom)

        binding.titleTv.text = args.name
        binding.profileImg.loadProfileUrl(args.avatar)

        binding.backBtn.onClick {
            findNavController().navigate(ChatRoomFragmentDirections.actionToHome())
        }

        binding.sendMessageBtn.onClick {
            val message = binding.messageEd.text.toString()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(message)
                binding.messageEd.setText("")
            }
        }

        binding.list.recycler.let {
            it.adapter = adapter
            it.apply {
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        viewModelAuth.getProfileUser.let {
            isLog("email user : ${it.email}")
            adapter.senderEmail(it.email)
        }

        adapter.clear()
        observe(viewModel.chatMessageResponse) {
            adapter.insertAll(it!!)
            isLog("size message : ${it.size}")
            isLog("data : $it")
        }

    }

    @Subscribe
    fun onReceiveComment(event: QiscusCommentReceivedEvent) {
        event.qiscusComment
        viewModel.setMessageLive(event.qiscusComment)
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this@ChatRoomFragment)
    }

    @Subscribe
    fun onReceiveChatRoomEvent(roomEvent: QiscusChatRoomEvent) {
        when (roomEvent.event) {
            QiscusChatRoomEvent.Event.DELIVERED -> {
                roomEvent.roomId // this is the room id
                roomEvent.user // this is the qiscus user id
                roomEvent.commentId // the comment id was delivered
            }
            QiscusChatRoomEvent.Event.READ -> {
                roomEvent.roomId // this is the room id
                roomEvent.user // this is the qiscus user id
                roomEvent.commentId // the comment id was read
            }
        }
    }

    @Subscribe
    fun onUserOnlinePresence(event: QiscusUserStatusEvent) {
        event.user // this is the qiscus user id
        event.isOnline // true if user is online
        event.lastActive // Date of last active user
    }


    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this@ChatRoomFragment)
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this@ChatRoomFragment)
//        QiscusPusherApi.getInstance().unsubscribeUserOnlinePresence(args)
        QiscusPusherApi.getInstance().unsubsribeChatRoom(args.chatRoom)
    }

    private fun isLog(msg: String) {
        Log.e("Chat", msg)
    }


}