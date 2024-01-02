package com.a1hd.movies.ui.sections.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.a1hd.movies.R
import com.a1hd.movies.ui.base.BaseFragment
import com.a1hd.movies.databinding.FragmentDashboardBinding
import com.a1hd.movies.ui.navigation.NavigationRouter
import com.a1hd.movies.ui.navigation.route.Router
import com.a1hd.movies.ui.repository.MovieType
import com.a1hd.movies.ui.repository.MoviesDataModel
import com.a1hd.movies.ui.sections.dashboard.adapter.DashboardRecyclerAdapter
import com.a1hd.movies.ui.sections.dashboard.viewpager.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val NONE_LINK_TO_DETAILS = "NONE"

@AndroidEntryPoint
class DashboardFragment: BaseFragment<FragmentDashboardBinding>(FragmentDashboardBinding::inflate) {

    @Inject
    lateinit var topMoviesRecyclerAdapter: DashboardRecyclerAdapter

    @Inject
    lateinit var topTvShowsRecyclerAdapter: DashboardRecyclerAdapter

    @Inject
    lateinit var moviesRecyclerAdapter: DashboardRecyclerAdapter

    @Inject
    lateinit var tvShowsRecyclerAdapter: DashboardRecyclerAdapter
    
    private val dashboardViewModel: DashboardViewModel by viewModels()

    private val viewPagerAdapter: ViewPagerAdapter by lazy {
        ViewPagerAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topMoviesRecyclerAdapter.onMovieClickListener = onMovieClickListener
        topTvShowsRecyclerAdapter.onMovieClickListener = onMovieClickListener
        moviesRecyclerAdapter.onMovieClickListener = onMovieClickListener
        tvShowsRecyclerAdapter.onMovieClickListener = onMovieClickListener
        viewPagerAdapter.onMostPopularMovieClickListener = {
            navigationRouter.navigateTo(Router.WatchMovie(it.link, MovieType.MOVIE))
        }
        binding.rvTopMovies.adapter = topMoviesRecyclerAdapter
        binding.rvTopTvShows.adapter = topTvShowsRecyclerAdapter
        binding.rvMovies.adapter = moviesRecyclerAdapter
        binding.rvTvShows.adapter = tvShowsRecyclerAdapter
        binding.viewPagerMain.adapter = viewPagerAdapter

        dashboardViewModel.fetchMostPopular()
        dashboardViewModel.fetchMostPopularLiveData.observe(viewLifecycleOwner) {
            viewPagerAdapter.setImages(it.toMutableList())
        }

        dashboardViewModel.fetchDashboard()
        dashboardViewModel.fetchDashboardMoviesLiveData.observe(viewLifecycleOwner) {
            binding.pbProgress.isVisible = false
            binding.nsMoviesList.isVisible = true

            topMoviesRecyclerAdapter.setMovies(it.filter { it.type == MovieType.MOVIE }.toMutableList())
            topTvShowsRecyclerAdapter.setMovies(it.filter { it.type == MovieType.TV_SHOW }.toMutableList())
        }

        dashboardViewModel.fetchMovies()
        dashboardViewModel.fetchMoviesLiveData.observe(viewLifecycleOwner) {
            val moviesList = it.toMutableList()
            moviesList.add(0, allMoviesPlaceHolder())
            moviesRecyclerAdapter.setMovies(moviesList)
        }

        dashboardViewModel.fetchTvShows()
        dashboardViewModel.fetchTvShowsLiveData.observe(viewLifecycleOwner) {
            val tvShowsList = it.toMutableList()
            tvShowsList.add(0, allTvShowsPlaceHolder())
            tvShowsRecyclerAdapter.setMovies(tvShowsList)
        }
    }

    private val onMovieClickListener: (MoviesDataModel) -> Unit = {
        if (it.link == "NONE") {
            if (it.type == MovieType.MOVIE) {
                navigationRouter.navigateTo(Router.AllMovies)
            } else if (it.type == MovieType.TV_SHOW) {
                navigationRouter.navigateTo(Router.AllTvShows)
            }
        } else {
            navigationRouter.navigateTo(Router.MovieDetails(it.link))
        }
    }

    private fun allMoviesPlaceHolder(): MoviesDataModel {
        val quality = ""
        val thumbnail = ""
        return MoviesDataModel(
            getString(R.string.all_movies),
            thumbnail,
            NONE_LINK_TO_DETAILS,
            MovieType.MOVIE,
            quality
        )
    }

    private fun allTvShowsPlaceHolder(): MoviesDataModel {
        val quality = ""
        val thumbnail = ""
        return MoviesDataModel(
            getString(R.string.all_tv_shows),
            thumbnail,
            NONE_LINK_TO_DETAILS,
            MovieType.TV_SHOW,
            quality
        )
    }
}