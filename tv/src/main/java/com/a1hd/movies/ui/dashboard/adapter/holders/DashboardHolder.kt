package com.a1hd.movies.ui.dashboard.adapter.holders

import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.databinding.ItemDashboardBinding
import com.a1hd.movies.ui.repository.MoviesDataModel
import com.bumptech.glide.Glide

class DashboardHolder(private val binding: ItemDashboardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(workoutWIthPulse: MoviesDataModel, onStatisticsClickListener: (MoviesDataModel) -> Unit) {
        binding.tvName.text = workoutWIthPulse.name
        Glide.with(itemView.context).load(workoutWIthPulse.thumbnail).into(binding.ivPoster)

        itemView.setOnClickListener {
            onStatisticsClickListener.invoke(workoutWIthPulse)
        }
    }
}