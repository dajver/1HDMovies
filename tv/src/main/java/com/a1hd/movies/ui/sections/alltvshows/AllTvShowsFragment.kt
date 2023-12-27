package com.a1hd.movies.ui.sections.alltvshows

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.a1hd.movies.databinding.FragmentAllTvShowsBinding
import com.a1hd.movies.ui.base.BaseFragment
import com.a1hd.movies.ui.sections.allmovies.AllMoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllTvShowsFragment: BaseFragment<FragmentAllTvShowsBinding>(FragmentAllTvShowsBinding::inflate) {

    private val allTvShowsViewModel: AllMoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}