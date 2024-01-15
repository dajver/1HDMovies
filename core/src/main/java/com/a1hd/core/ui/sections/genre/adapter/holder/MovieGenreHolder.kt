package com.a1hd.core.ui.sections.genre.adapter.holder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.core.api.repository.MovieType
import com.a1hd.core.api.repository.MoviesDataModel
import com.a1hd.core.databinding.ItemDashboardBinding
import com.bumptech.glide.Glide

class MovieGenreHolder(private val binding: ItemDashboardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieData: MoviesDataModel, onStatisticsClickListener: (MoviesDataModel) -> Unit) {
        binding.tvName.text = movieData.name
        binding.tvQuality.text = movieData.quality

        binding.ivPoster.isVisible = movieData.thumbnail.isNotEmpty()
        if (movieData.thumbnail.isNotEmpty()) {
            Glide.with(itemView.context).load(movieData.thumbnail).into(binding.ivPoster)
        }

        val isTitleVisible = movieData.thumbnail.isEmpty() && movieData.quality.isEmpty()
        binding.tvTitle.isVisible = isTitleVisible
        binding.tvTitle.text = movieData.name
        binding.tvOther.text = movieData.other
        binding.tvOther.isVisible = movieData.type == MovieType.TV_SHOW
        binding.tvReleaseYear.text = movieData.other
        binding.tvReleaseYear.isVisible = movieData.type == MovieType.MOVIE

        itemView.setOnClickListener {
            onStatisticsClickListener.invoke(movieData)
        }
    }
}