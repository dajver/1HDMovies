package com.a1hd.movies.ui.sections.movie.adapter.season.holder

import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.R
import com.a1hd.movies.api.repository.MovieSeasonDataModel
import com.a1hd.movies.databinding.ItemSeasonBinding

class SeasonsHolder(private val binding: ItemSeasonBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movieData: MovieSeasonDataModel) {
        if (movieData.isSelected) {
            binding.tvSeason.setTextColor(ContextCompat.getColor(itemView.context, R.color.selected_background))
            binding.tvSeason.setTypeface(binding.tvSeason.typeface, Typeface.BOLD)
        } else {
            binding.tvSeason.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            binding.tvSeason.setTypeface(binding.tvSeason.typeface, Typeface.NORMAL)
        }

        binding.tvSeason.text = String.format("%s %s", "Season", movieData.seasonNumber)
    }

    fun select(hasFocus: Boolean, movieData: MovieSeasonDataModel) {
        if (hasFocus && movieData.isSelected) {
            binding.tvSeason.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
        } else {
            if (movieData.isSelected) {
                binding.tvSeason.setTextColor(ContextCompat.getColor(itemView.context, R.color.selected_background))
            } else {
                binding.tvSeason.setTextColor(ContextCompat.getColor(itemView.context, android.R.color.white))
            }
        }
    }
}