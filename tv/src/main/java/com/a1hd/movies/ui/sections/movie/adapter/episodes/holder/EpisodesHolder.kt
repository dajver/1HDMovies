package com.a1hd.movies.ui.sections.movie.adapter.episodes.holder

import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.api.repository.MovieEpisodesDataModel
import com.a1hd.movies.databinding.ItemEpisodeBinding

class EpisodesHolder(private val binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieData: MovieEpisodesDataModel, onEpisodeClickListener: (MovieEpisodesDataModel) -> Unit) {
        binding.tvEpisode.text = movieData.episodeNumber
        binding.tvEpisodeName.text = movieData.episodeName

        itemView.setOnClickListener {
            onEpisodeClickListener.invoke(movieData)
        }
    }
}