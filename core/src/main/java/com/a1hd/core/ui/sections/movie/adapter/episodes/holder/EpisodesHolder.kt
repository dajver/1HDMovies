package com.a1hd.core.ui.sections.movie.adapter.episodes.holder

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.core.R
import com.a1hd.core.api.repository.MovieEpisodesDataModel
import com.a1hd.core.databinding.ItemEpisodeBinding

class EpisodesHolder(private val binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieData: MovieEpisodesDataModel) {
        if (movieData.isSelected) {
            binding.tvEpisode.setTextColor(ContextCompat.getColor(itemView.context, R.color.selected_background))
            binding.tvEpisode.setTypeface(binding.tvEpisode.typeface, Typeface.BOLD)
        } else {
            binding.tvEpisode.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            binding.tvEpisode.setTypeface(binding.tvEpisode.typeface, Typeface.NORMAL)
        }

        binding.tvEpisode.text = movieData.episodeNumber
        binding.tvEpisodeName.text = movieData.episodeName
    }

    fun select(hasFocus: Boolean, movieData: MovieEpisodesDataModel) {
        if (hasFocus && movieData.isSelected) {
            binding.tvEpisode.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
        } else {
            if (movieData.isSelected) {
                binding.tvEpisode.setTextColor(ContextCompat.getColor(itemView.context, R.color.selected_background))
            } else {
                binding.tvEpisode.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            }
        }
    }
}