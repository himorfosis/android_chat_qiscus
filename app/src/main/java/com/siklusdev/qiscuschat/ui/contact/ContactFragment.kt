package com.siklusdev.qiscuschat.ui.contact

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.siklusdev.qiscuschat.databinding.FragmentContactBinding
import com.siklusdev.qiscuschat.module.BaseFragmentBinding
import com.siklusdev.qiscuschat.repo.ChatViewModel
import com.siklusdev.qiscuschat.repo.ContactViewModel
import com.siklusdev.qiscuschat.ui.contact.adapter.ContactAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment: BaseFragmentBinding<FragmentContactBinding>(FragmentContactBinding::inflate) {

    private val viewModel by viewModels<ContactViewModel>()
    private val viewModelChat by viewModels<ChatViewModel>()

    private val adapter by lazy(LazyThreadSafetyMode.NONE) {
        ContactAdapter(
            ::onClickDetail
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getContacts()

        binding.list.recycler.let {
            it.adapter = adapter
            it.apply {
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(requireContext())
            }
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

        adapter.clear()
        observe(viewModel.contactResponse) { account ->

            binding.list.let {
                it.emptyTitleTv.isVisible = account!!.isEmpty()
                it.emptyDescriptionTv.isVisible = account!!.isEmpty()
            }

            adapter.insertAll(account)
        }

        observe(viewModelChat.createChatResponse) {
            findNavController().navigate(ContactFragmentDirections.actionToChatRoom(it?.id!!.toInt(), it.name, it.avatarUrl, it))
        }

    }

    private fun onClickDetail(data: QiscusAccount) {
        viewModelChat.createChatRoom(data.email)
    }

}