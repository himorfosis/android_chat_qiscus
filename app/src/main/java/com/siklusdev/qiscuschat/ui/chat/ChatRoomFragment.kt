package com.siklusdev.qiscuschat.ui.chat

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.siklusdev.qiscuschat.databinding.FragmentChatRoomBinding
import com.siklusdev.qiscuschat.module.BaseFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.sdk27.coroutines.onClick

@AndroidEntryPoint
class ChatRoomFragment : BaseFragmentBinding<FragmentChatRoomBinding>(FragmentChatRoomBinding::inflate) {

    private val args by navArgs<ChatRoomFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleTv.text = args.name

        binding.backBtn.onClick {

        }


    }

}