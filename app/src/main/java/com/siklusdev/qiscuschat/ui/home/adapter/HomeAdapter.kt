package com.siklusdev.qiscuschat.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.siklusdev.qiscuschat.databinding.ItemChatRoomBinding
import com.siklusdev.qiscuschat.extensions.getLastMessageTimestamp
import com.siklusdev.qiscuschat.extensions.loadProfileUrl
import org.jetbrains.anko.sdk27.coroutines.onClick

class HomeAdapter(
    private val onClick: (QiscusChatRoom) -> Unit
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var listData: MutableList<QiscusChatRoom> = ArrayList()
    var listFilter : MutableList<QiscusChatRoom> = ArrayList()

    fun insertAll(data: List<QiscusChatRoom?>?) {
        data?.forEach {
            listData.add(it!!)
            notifyItemInserted(listData.size - 1)
        }
        listFilter = listData as ArrayList<QiscusChatRoom>

    }

    fun clear() {
        if (listData.isNotEmpty()) {
            listData.clear()
            listFilter.clear()
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

    override fun getItemCount() = listFilter.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listFilter[position]
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
            binding.timeTv.text = item.lastComment.time.getLastMessageTimestamp()

            if (item.lastComment.sender != null) {
                binding.lastMessageTv.text = item.lastComment.message
            }

        }

    }

    fun getFilter(): Filter {

        return object : Filter() {

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listFilter = results?.values as ArrayList<QiscusChatRoom>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint.toString().toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString.isEmpty())
                    listFilter = listData
                else {
                    val resultList = ArrayList<QiscusChatRoom>()
                    listData.forEach {
                        if (it.name.toLowerCase().contains(queryString)) {
                            resultList.add(it)
                        }
                    }
                    listFilter = resultList
                }

                filterResults.values = listFilter
                return filterResults

            }

        }
    }

}