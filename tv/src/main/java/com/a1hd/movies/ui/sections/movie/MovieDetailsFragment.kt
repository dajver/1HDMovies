package com.a1hd.movies.ui.sections.movie

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.a1hd.movies.ui.base.BaseFragment
import com.a1hd.movies.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment<FragmentMovieDetailsBinding>(FragmentMovieDetailsBinding::inflate) {

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}