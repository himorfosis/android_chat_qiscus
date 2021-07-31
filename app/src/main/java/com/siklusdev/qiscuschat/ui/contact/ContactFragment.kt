package com.siklusdev.qiscuschat.ui.contact

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.siklusdev.qiscuschat.databinding.FragmentContactBinding
import com.siklusdev.qiscuschat.module.BaseFragmentBinding
import com.siklusdev.qiscuschat.repo.ContactViewModel
import com.siklusdev.qiscuschat.ui.contact.adapter.ContactAdapter

class ContactFragment: BaseFragmentBinding<FragmentContactBinding>(FragmentContactBinding::inflate) {

    private val viewModel by viewModels<ContactViewModel>()

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

        observe(viewModel.contactResponse) { account ->

            binding.list.let {
                it.emptyTitleTv.isVisible = account!!.isEmpty()
                it.emptyDescriptionTv.isVisible = account!!.isEmpty()
            }

            adapter.insertAll(account)
        }

    }

    private fun onClickDetail(data: QiscusAccount) {
//        findNavController().navigate(Contac)
    }

}