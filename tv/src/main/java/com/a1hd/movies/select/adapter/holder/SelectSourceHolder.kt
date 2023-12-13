package com.a1hd.movies.select.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.databinding.ItemSourceBinding

class SelectSourceHolder (private val viewBinding: ItemSourceBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(source: String, onSourceClickListener: (source: String) -> Unit) {
        viewBinding.tvSource.text = source
        viewBinding.tvSource.setOnClickListener {
            onSourceClickListener.invoke(source)
        }
    }
}