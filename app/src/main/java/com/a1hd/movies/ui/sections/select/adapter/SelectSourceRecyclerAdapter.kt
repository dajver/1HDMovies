package com.a1hd.movies.ui.sections.select.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.databinding.ItemSourceBinding
import com.a1hd.movies.ui.sections.select.adapter.holder.SelectSourceHolder

class SelectSourceRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onSourceClickListener: (exercise: String) -> Unit = {}
    private var sourcesList = mutableListOf<String>()

    fun setSourceList(sourcesList: MutableList<String>) {
        this.sourcesList = sourcesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectSourceHolder {
        return SelectSourceHolder(ItemSourceBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as SelectSourceHolder
        holder.bind(sourcesList[position], onSourceClickListener)
        holder.itemView.setOnFocusChangeListener { v, hasFocus ->
            v.isSelected = hasFocus
        }
    }

    override fun getItemCount(): Int {
        return sourcesList.size
    }
}