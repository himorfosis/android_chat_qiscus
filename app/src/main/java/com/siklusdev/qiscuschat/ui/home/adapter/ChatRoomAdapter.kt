package com.siklusdev.qiscuschat.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.siklusdev.qiscuschat.databinding.ItemChatRoomBinding
import com.siklusdev.qiscuschat.extensions.loadProfileUrl
import org.jetbrains.anko.sdk27.coroutines.onClick

class ChatRoomAdapter(
    private val onClick: (QiscusChatRoom) -> Unit
) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    var listData: MutableList<QiscusChatRoom> = ArrayList()

    fun insertAll(data: List<QiscusChatRoom?>?) {
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
            ItemChatRoomBinding.inflate(
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

    inner class ViewHolder(val binding: ItemChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: QiscusChatRoom) {

            binding.avatarImg.loadProfileUrl(item.avatarUrl)
            binding.nameTv.text = item.name

            if (item.lastComment.sender != null) {
                var lastMessageText: String? =
                    if (item.lastComment.isMyComment) "You: "
                    else item.lastComment.sender.split(" ").toTypedArray()[0] + ": "
                binding.lastMessageTv.text = lastMessageText
            }

        }

    }

}