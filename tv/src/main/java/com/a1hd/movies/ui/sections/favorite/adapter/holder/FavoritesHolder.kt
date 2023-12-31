package com.a1hd.movies.ui.sections.favorite.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.api.repository.MoviesDetailsDataModel
import com.a1hd.movies.databinding.ItemDashboardBinding
import com.bumptech.glide.Glide

class FavoritesHolder(private val binding: ItemDashboardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieData: MoviesDetailsDataModel, onFavoriteCLickListener: (MoviesDetailsDataModel) -> Unit) {
        binding.tvName.text = movieData.name
        binding.tvQuality.text = movieData.quality
        Glide.with(itemView.context).load(movieData.thumbnail).into(binding.ivPoster)

        itemView.setOnClickListener {
            onFavoriteCLickListener.invoke(movieData)
        }
    }
}