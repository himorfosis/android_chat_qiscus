package com.siklusdev.qiscuschat.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.siklusdev.qiscuschat.databinding.FragmentHomeBinding
import com.siklusdev.qiscuschat.module.BaseFragmentBinding
import com.siklusdev.qiscuschat.repo.ChatViewModel
import com.siklusdev.qiscuschat.ui.home.adapter.ChatRoomAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.sdk27.coroutines.onClick

@AndroidEntryPoint
class HomeFragment : BaseFragmentBinding<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<ChatViewModel>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ChatRoomAdapter(
            ::onClickDetail
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.onClick {
//            findNavController().navigate(HomeFragmentDirections.ac)
        }

        binding.list.recycler.let {
            it.adapter = adapter
            it.apply {
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        observe(viewModel.chatRoomResponse) { chat ->

            binding.list.let {
                it.emptyTitleTv.isVisible = chat!!.isEmpty()
                it.emptyDescriptionTv.isVisible = chat!!.isEmpty()
            }

            adapter.insertAll(chat)

        }

    }

    private fun onClickDetail(data: QiscusChatRoom) {
        findNavController().navigate(HomeFragmentDirections.actionToChatRoom(data.id.toInt(), data.name, data.avatarUrl))
    }

}