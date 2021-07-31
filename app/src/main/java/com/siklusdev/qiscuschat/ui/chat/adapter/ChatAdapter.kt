package com.siklusdev.qiscuschat.ui.chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.qiscus.sdk.chat.core.data.model.QiscusComment
import com.siklusdev.qiscuschat.R
import com.siklusdev.qiscuschat.databinding.ItemMyChatBinding
import com.siklusdev.qiscuschat.databinding.ItemYourChatBinding
import com.siklusdev.qiscuschat.extensions.toFullDate
import com.siklusdev.qiscuschat.extensions.toTime

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listData: MutableList<QiscusComment> = ArrayList()
    var MY_CHAT_MAIL = ""

    fun senderEmail(email: String) {
        MY_CHAT_MAIL = email
    }

    fun insertAll(data: List<QiscusComment>) {
        data.forEach {
            listData.add(it)
            notifyItemInserted(listData.size - 1)
        }
    }

    fun clear() {
        if (listData.isNotEmpty()) {
            listData.clear()
            notifyDataSetChanged()
        }
    }

    companion object {
        const val TYPE_MY_CHAT = 1
        const val TYPE_YOUR_CHAT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_MY_CHAT -> MyChatViewHolder(
                ItemMyChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> YourChatViewHolder(
                ItemYourChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = listData[position]
        return when (message.type) {
            QiscusComment.Type.TEXT -> if (MY_CHAT_MAIL == message.senderEmail) TYPE_MY_CHAT else TYPE_YOUR_CHAT
            else -> if (MY_CHAT_MAIL == message.senderEmail) TYPE_MY_CHAT else TYPE_YOUR_CHAT
        }
    }

    override fun getItemCount() = listData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = listData[position]

//        if (item.isMyComment) {
        if (MY_CHAT_MAIL == item.senderEmail) {
            (holder as MyChatViewHolder)
            holder.binding.dateMessageTv.isVisible = position == 0
            holder.bindTo(item)
        } else {
            (holder as YourChatViewHolder)
            holder.binding.dateMessageTv.isVisible = position == 0
            holder.bindTo(item)
        }

    }

    inner class MyChatViewHolder(val binding: ItemMyChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(data: QiscusComment) {

            binding.dateMessageTv.text = data.time.toFullDate()
            binding.messageTv.text = data.message
            binding.dateTimeSendTv.text = data.time.toTime()

            binding.statusChatImg.setImageResource(
                when (data.state) {
                    QiscusComment.STATE_PENDING -> R.drawable.ic_chat_pending
                    QiscusComment.STATE_ON_QISCUS -> R.drawable.ic_chat_sending
                    QiscusComment.STATE_DELIVERED -> R.drawable.ic_chat_read
                    QiscusComment.STATE_READ -> R.drawable.ic_chat_read
                    else -> R.drawable.ic_chat_sending_failed
                }
            )

        }

    }

    inner class YourChatViewHolder(val binding: ItemYourChatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(data: QiscusComment) {

            binding.dateMessageTv.text = data.time.toFullDate()
            binding.messageTv.text = data.message
            binding.dateTimeSendTv.text = data.time.toTime()

        }

    }


}