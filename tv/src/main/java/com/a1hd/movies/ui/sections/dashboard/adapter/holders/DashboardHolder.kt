package com.a1hd.movies.ui.sections.dashboard.adapter.holders

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.R
import com.a1hd.movies.databinding.ItemDashboardBinding
import com.a1hd.movies.ui.repository.MovieType
import com.a1hd.movies.ui.repository.MoviesDataModel
import com.bumptech.glide.Glide

class DashboardHolder(private val binding: ItemDashboardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieData: MoviesDataModel, onStatisticsClickListener: (MoviesDataModel) -> Unit) {
        binding.tvName.text = movieData.name
        binding.tvType.text = if (movieData.type == MovieType.MOVIE) {
            itemView.context.getString(R.string.movies)
        } else {
            itemView.context.getString(R.string.tv_shows)
        }
        binding.tvQuality.text = movieData.quality
        Glide.with(itemView.context).load(movieData.thumbnail).into(binding.ivPoster)

        itemView.setOnClickListener {
            onStatisticsClickListener.invoke(movieData)
        }
    }

    fun showTitle(isNeedShowTitle: Boolean) {
        binding.tvType.visibility = if (isNeedShowTitle) View.VISIBLE else View.INVISIBLE
    }
}