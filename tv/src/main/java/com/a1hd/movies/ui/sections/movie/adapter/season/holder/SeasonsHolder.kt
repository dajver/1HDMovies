package com.a1hd.movies.ui.sections.movie.adapter.season.holder

import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.api.repository.MovieSeasonDataModel
import com.a1hd.movies.databinding.ItemSeasonBinding

class SeasonsHolder(private val binding: ItemSeasonBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieData: MovieSeasonDataModel, onSeasonClickListener: (MovieSeasonDataModel) -> Unit) {
        binding.tvSeason.text = String.format("%s %s", "Season", movieData.seasonNumber)

        itemView.setOnClickListener {
            onSeasonClickListener.invoke(movieData)
        }
    }
}