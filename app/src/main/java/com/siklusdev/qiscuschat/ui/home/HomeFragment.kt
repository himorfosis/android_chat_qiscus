package com.siklusdev.qiscuschat.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.siklusdev.qiscuschat.databinding.FragmentHomeBinding
import com.siklusdev.qiscuschat.extensions.loadProfileUrl
import com.siklusdev.qiscuschat.module.BaseFragmentBinding
import com.siklusdev.qiscuschat.repo.AuthViewModel
import com.siklusdev.qiscuschat.repo.ChatViewModel
import com.siklusdev.qiscuschat.ui.home.adapter.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.sdk27.coroutines.onClick

@AndroidEntryPoint
class HomeFragment : BaseFragmentBinding<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<ChatViewModel>()
    private val viewModelAuth by viewModels<AuthViewModel>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        HomeAdapter(
            ::onClickDetail
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getChatAllRooms()

        binding.list.recycler.let {
            it.adapter = adapter
            it.apply {
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

        binding.accountBtn.onClick {
            findNavController().navigate(HomeFragmentDirections.actionToContact())
        }

        binding.profileImg.onClick {
            findNavController().navigate(HomeFragmentDirections.actionToProfile())
        }

        binding.searchEd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(data: Editable?) {
                adapter.getFilter().filter(data.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        viewModelAuth.getProfileUser.let {
            binding.profileImg.loadProfileUrl(it.avatar)
        }

        observe(viewModel.chatRoomResponse) { chat ->

            binding.list.let {
                it.emptyTitleTv.isVisible = chat!!.isEmpty()
                it.emptyDescriptionTv.isVisible = chat!!.isEmpty()
            }

            adapter.clear()
            adapter.insertAll(chat)

        }

    }

    private fun onClickDetail(data: QiscusChatRoom) {
        findNavController().navigate(HomeFragmentDirections.actionToChatRoom(data.id.toInt(), data.name, data.avatarUrl, data))
    }

}