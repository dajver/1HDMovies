package com.a1hd.movies.ui.sections.select.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.a1hd.movies.R
import com.a1hd.movies.databinding.ItemSourceBinding

class SelectSourceHolder (private val viewBinding: ItemSourceBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(source: String, onSourceClickListener: (source: String) -> Unit) {
        val position = adapterPosition + 1
        viewBinding.tvSource.text = itemView.context.getString(R.string.available_source, position)
        viewBinding.tvQuality.text = when (position) {
            1 -> "720p"
            2 -> "1080p"
            3 -> "480p"
            else -> "320p"
        }
        viewBinding.tvSubtitles.text = when (position) {
            1 -> "With subtitles"
            2 -> "With multiple subtitles"
            3 -> "No subtitles"
            else -> "Original"
        }
        itemView.setOnClickListener {
            onSourceClickListener.invoke(source)
        }
    }
}