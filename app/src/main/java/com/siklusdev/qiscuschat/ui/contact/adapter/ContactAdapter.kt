package com.siklusdev.qiscuschat.ui.contact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.siklusdev.qiscuschat.databinding.ItemContactBinding
import com.siklusdev.qiscuschat.extensions.loadProfileUrl
import org.jetbrains.anko.sdk27.coroutines.onClick

class ContactAdapter(
    private val onClick: (QiscusAccount) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var listData: MutableList<QiscusAccount> = ArrayList()

    fun insertAll(data: List<QiscusAccount?>?) {
        data?.forEach {
            listData.add(it!!)
            notifyItemInserted(listData.size - 1)
        }
    }

    fun clear() {
        if (listData.isNotEmpty()) {
            listData.clear()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listData[position]
        holder.bindTo(item)

        holder.itemView.onClick {
            onClick(item)
        }

    }

    inner class ViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: QiscusAccount) {

            binding.avatarImg.loadProfileUrl(item.avatar)
            binding.nameTv.text = item.username

        }

    }

}