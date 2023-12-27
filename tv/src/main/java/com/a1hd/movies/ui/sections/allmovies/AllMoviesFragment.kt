package com.a1hd.movies.ui.sections.allmovies

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.a1hd.movies.databinding.FragmentAllMoviesBinding
import com.a1hd.movies.ui.base.BaseFragment
import com.a1hd.movies.ui.navigation.route.Router
import com.a1hd.movies.ui.sections.allmovies.adapter.AllMoviesRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllMoviesFragment: BaseFragment<FragmentAllMoviesBinding>(FragmentAllMoviesBinding::inflate) {

    @Inject
    lateinit var allMoviesRecyclerAdapter: AllMoviesRecyclerAdapter

    private val allMoviesViewModel: AllMoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        allMoviesRecyclerAdapter.onMovieClickListener = {
            navigationRouter.navigateTo(Router.MovieDetails(it.link))
        }
        binding.rvMovies.adapter = allMoviesRecyclerAdapter

        allMoviesViewModel.fetchMovies()
        allMoviesViewModel.fetchMoviesLiveData.observe(viewLifecycleOwner) {
            val moviesList = it.toMutableList()
            allMoviesRecyclerAdapter.setMovies(moviesList)
        }
    }
}