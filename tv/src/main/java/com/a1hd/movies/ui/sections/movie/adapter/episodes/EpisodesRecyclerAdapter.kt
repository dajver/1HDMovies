package com.a1hd.movies.ui.sections.movie.adapter.episodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.api.repository.MovieEpisodesDataModel
import com.a1hd.movies.databinding.ItemEpisodeBinding
import com.a1hd.movies.ui.sections.movie.adapter.episodes.holder.EpisodesHolder
import javax.inject.Inject

class EpisodesRecyclerAdapter @Inject constructor(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var episodesList: MutableList<MovieEpisodesDataModel> = mutableListOf()
    var onEpisodeClickListener: (MovieEpisodesDataModel) -> Unit = { }

    fun setEpisodes(groups: MutableList<MovieEpisodesDataModel>) {
        this.episodesList = groups
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodesHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = (holder as EpisodesHolder)
        viewHolder.bind(episodesList[position], onEpisodeClickListener)
        viewHolder.itemView.setOnFocusChangeListener { v, hasFocus ->
            v.isSelected = hasFocus
        }
    }

    override fun getItemCount(): Int = episodesList.size
}