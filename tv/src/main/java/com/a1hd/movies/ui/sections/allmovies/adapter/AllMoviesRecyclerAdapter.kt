package com.a1hd.movies.ui.sections.allmovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.databinding.ItemDashboardBinding
import com.a1hd.movies.ui.repository.MoviesDataModel
import com.a1hd.movies.ui.sections.allmovies.adapter.holder.AllMoviesHolder
import javax.inject.Inject

class AllMoviesRecyclerAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var moviesList: MutableList<MoviesDataModel> = mutableListOf()
    var onMovieClickListener: (MoviesDataModel) -> Unit = { }

    fun setMovies(groups: MutableList<MoviesDataModel>) {
        this.moviesList = groups
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllMoviesHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = (holder as AllMoviesHolder)
        viewHolder.bind(moviesList[position], onMovieClickListener)
        viewHolder.itemView.setOnFocusChangeListener { v, hasFocus ->
            v.isSelected = hasFocus
        }
    }

    override fun getItemCount(): Int = moviesList.size
}