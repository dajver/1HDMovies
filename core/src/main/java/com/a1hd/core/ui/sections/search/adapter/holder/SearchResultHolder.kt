package com.a1hd.core.ui.sections.search.adapter.holder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.core.api.repository.MovieType
import com.a1hd.core.api.repository.MoviesDataModel
import com.a1hd.core.databinding.ItemDashboardBinding
import com.bumptech.glide.Glide

class SearchResultHolder(private val binding: ItemDashboardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieData: MoviesDataModel, onStatisticsClickListener: (MoviesDataModel) -> Unit) {
        binding.tvName.text = movieData.name
        binding.tvQuality.text = movieData.quality
        binding.tvOther.text = movieData.other
        binding.tvOther.isVisible = movieData.type == MovieType.TV_SHOW
        binding.tvReleaseYear.text = movieData.other
        binding.tvReleaseYear.isVisible = movieData.type == MovieType.MOVIE
        Glide.with(itemView.context).load(movieData.thumbnail).into(binding.ivPoster)

        itemView.setOnClickListener {
            onStatisticsClickListener.invoke(movieData)
        }
    }
}