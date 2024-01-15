package com.a1hd.core.ui.sections.alltvshows.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.core.api.repository.MoviesDataModel
import com.a1hd.core.databinding.ItemDashboardBinding
import com.a1hd.core.ui.sections.alltvshows.adapter.holder.AllTvShowsHolder
import javax.inject.Inject

class AllTvShowsRecyclerAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var tvSHowsList: MutableList<MoviesDataModel> = mutableListOf()
    var onTvShowsClickListener: (MoviesDataModel) -> Unit = { }

    fun setTvSHows(groups: MutableList<MoviesDataModel>) {
        this.tvSHowsList.addAll(groups)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllTvShowsHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = (holder as AllTvShowsHolder)
        viewHolder.bind(tvSHowsList[position], onTvShowsClickListener)
        viewHolder.itemView.setOnFocusChangeListener { v, hasFocus ->
            v.isSelected = hasFocus
        }
    }

    override fun getItemCount(): Int = tvSHowsList.size
}