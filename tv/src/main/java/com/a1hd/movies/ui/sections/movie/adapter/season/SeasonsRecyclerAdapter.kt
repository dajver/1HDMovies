package com.a1hd.movies.ui.sections.movie.adapter.season

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.api.repository.MovieSeasonDataModel
import com.a1hd.movies.databinding.ItemSeasonBinding
import com.a1hd.movies.ui.sections.movie.adapter.season.holder.SeasonsHolder
import javax.inject.Inject

class SeasonsRecyclerAdapter @Inject constructor(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var seasonsList: MutableList<MovieSeasonDataModel> = mutableListOf()
    var onSeasonClickListener: (MovieSeasonDataModel) -> Unit = { }

    fun setSeasons(groups: MutableList<MovieSeasonDataModel>) {
        this.seasonsList = groups
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemSeasonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeasonsHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = (holder as SeasonsHolder)
        viewHolder.bind(seasonsList[position], onSeasonClickListener)
        viewHolder.itemView.setOnFocusChangeListener { v, hasFocus ->
            v.isSelected = hasFocus
        }
    }

    override fun getItemCount(): Int = seasonsList.size
}