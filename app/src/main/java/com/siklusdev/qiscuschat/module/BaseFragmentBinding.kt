package com.siklusdev.qiscuschat.module

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import androidx.lifecycle.Observer

abstract class BaseFragmentBinding<VBinding: ViewBinding>(val inflate: CustomInflate<VBinding>): Fragment() {

    protected lateinit var binding: VBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
        liveData.observe(this, Observer(body))
    }

    inline fun <T : Any, L : LiveData<T>> Fragment.observeNonNull(
        liveData: L,
        crossinline body: (T) -> Unit
    ) {
        liveData.observe(viewLifecycleOwner, Observer { it?.let(body) })
    }

}