package com.a1hd.movies.ui.sections.dashboard.adapter.holders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.databinding.ItemDashboardBinding
import com.a1hd.movies.api.repository.MoviesDataModel
import com.bumptech.glide.Glide

class DashboardHolder(private val binding: ItemDashboardBinding) : RecyclerView.ViewHolder(binding.root) {

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

        itemView.setOnClickListener {
            onStatisticsClickListener.invoke(movieData)
        }
    }
}