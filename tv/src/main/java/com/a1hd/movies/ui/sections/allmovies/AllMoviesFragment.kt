package com.a1hd.movies.ui.sections.allmovies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.a1hd.movies.databinding.FragmentAllMoviesBinding
import com.a1hd.movies.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMoviesFragment: BaseFragment<FragmentAllMoviesBinding>(FragmentAllMoviesBinding::inflate) {

    private val allMoviesViewModel: AllMoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}