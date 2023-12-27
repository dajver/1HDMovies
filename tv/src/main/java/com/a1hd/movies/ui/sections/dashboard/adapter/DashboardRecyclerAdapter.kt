package com.a1hd.movies.ui.sections.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.databinding.ItemDashboardBinding
import com.a1hd.movies.ui.sections.dashboard.adapter.holders.DashboardHolder
import com.a1hd.movies.ui.repository.MoviesDataModel
import javax.inject.Inject

class DashboardRecyclerAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var moviesList: MutableList<MoviesDataModel> = mutableListOf()
    var onMovieClickListener: (MoviesDataModel) -> Unit = { }

    fun setMovies(groups: MutableList<MoviesDataModel>) {
        this.moviesList = groups
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = (holder as DashboardHolder)
        viewHolder.bind(moviesList[position], onMovieClickListener)
        viewHolder.itemView.setOnFocusChangeListener { v, hasFocus ->
            v.isSelected = hasFocus
        }
    }

    override fun getItemCount(): Int = moviesList.size
}