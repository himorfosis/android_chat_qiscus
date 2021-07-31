package com.siklusdev.qiscuschat.ui.contact.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.qiscus.sdk.chat.core.data.model.QiscusAccount
import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom
import com.siklusdev.qiscuschat.databinding.ItemContactBinding
import com.siklusdev.qiscuschat.extensions.loadProfileUrl
import org.jetbrains.anko.sdk27.coroutines.onClick

class ContactAdapter(
    private val onClick: (QiscusAccount) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    var listData: MutableList<QiscusAccount> = ArrayList()
    var listFilter : MutableList<QiscusAccount> = ArrayList()

    fun insertAll(data: List<QiscusAccount?>?) {
        data?.forEach {
            listData.add(it!!)
            notifyItemInserted(listData.size - 1)
        }
        listFilter = listData as ArrayList<QiscusAccount>
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
            ItemContactBinding.inflate(
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

    inner class ViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: QiscusAccount) {

            binding.avatarImg.loadProfileUrl(item.avatar)
            binding.nameTv.text = item.username

        }

    }

    fun getFilter(): Filter {

        return object : Filter() {

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listFilter = results?.values as ArrayList<QiscusAccount>
                notifyDataSetChanged()
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint.toString().toLowerCase()

                val filterResults = FilterResults()
                filterResults.values = if (queryString.isEmpty())
                    listFilter = listData
                else {
                    val resultList = ArrayList<QiscusAccount>()
                    listData.forEach {
                        if (it.username.toLowerCase().contains(queryString)) {
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