package com.a1hd.core.ui.sections.allmovies.adapter.holder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.core.api.repository.MoviesDataModel
import com.a1hd.core.databinding.ItemDashboardBinding
import com.bumptech.glide.Glide

class AllMoviesHolder(private val binding: ItemDashboardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieData: MoviesDataModel, onStatisticsClickListener: (MoviesDataModel) -> Unit) {
        binding.tvName.text = movieData.name
        binding.tvQuality.text = movieData.quality
        binding.tvReleaseYear.text = movieData.other
        binding.tvOther.isVisible = false
        Glide.with(itemView.context).load(movieData.thumbnail).into(binding.ivPoster)

        itemView.setOnClickListener {
            onStatisticsClickListener.invoke(movieData)
        }
    }
}