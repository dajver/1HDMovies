package com.a1hd.movies.ui.sections.movie

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.a1hd.movies.R
import com.a1hd.movies.api.repository.MovieType
import com.a1hd.movies.databinding.FragmentMovieDetailsBinding
import com.a1hd.movies.ui.base.BaseFragment
import com.a1hd.movies.ui.navigation.route.Router
import com.a1hd.movies.api.repository.MoviesDetailsDataModel
import com.a1hd.movies.ui.sections.movie.adapter.episodes.EpisodesRecyclerAdapter
import com.a1hd.movies.ui.sections.movie.adapter.season.SeasonsRecyclerAdapter
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment: BaseFragment<FragmentMovieDetailsBinding>(FragmentMovieDetailsBinding::inflate) {

    @Inject
    lateinit var seasonsRecyclerAdapter: SeasonsRecyclerAdapter

    @Inject
    lateinit var episodesRecyclerAdapter: EpisodesRecyclerAdapter

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    var movieUrl: String? = null
    private var movieDetailsModel: MoviesDetailsDataModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieUrl = it.getString(ARG_MOVIE_URL, "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (movieUrl.isNullOrEmpty()) {
            throw RuntimeException("movieUrl mustn't be null or empty")
        }

        seasonsRecyclerAdapter.onSeasonClickListener = {
            episodesRecyclerAdapter.setEpisodes(it.episodes)
        }
        episodesRecyclerAdapter.onEpisodeClickListener = {
            navigationRouter.navigateTo(Router.WatchMovie(it.link, MovieType.TV_SHOW))
        }
        binding.rvSeasons.adapter = seasonsRecyclerAdapter
        binding.rvEpisode.adapter = episodesRecyclerAdapter

        movieDetailsViewModel.fetchDetails(movieUrl!!)
        movieDetailsViewModel.fetchDetailsMoviesLiveData.observe(viewLifecycleOwner) {
            binding.pbProgress.isVisible = false
            binding.llMovieContainer.isVisible = true

            binding.tvName.text = it.name
            binding.tvDescription.text = it.description
            binding.tvCasts.text = it.cast
            binding.tvGenre.text = it.genre
            binding.tvDuration.text = getString(R.string.min, it.duration)
            binding.tvCountry.text = it.country
            binding.tvIMDB.text = it.imdb
            binding.tvRelease.text = it.release
            binding.tvProduction.text = it.production
            Glide.with(requireContext()).load(it.thumbnail).into(binding.ivPoster)

            binding.btnWatchMovie.isVisible = it.seasonsList.isNullOrEmpty()
            binding.rvSeasons.isVisible = !it.seasonsList.isNullOrEmpty()
            binding.rvEpisode.isVisible = !it.seasonsList?.lastOrNull()?.episodes.isNullOrEmpty()

            val seasons = it.seasonsList
            if (!seasons.isNullOrEmpty()) {
                seasonsRecyclerAdapter.setSeasons(seasons)

                val episodes = seasons.lastOrNull()?.episodes
                if (!episodes.isNullOrEmpty()) {
                    episodesRecyclerAdapter.setEpisodes(episodes)
                }
            }

            movieDetailsModel = it
        }

        binding.btnWatchMovie.setOnClickListener {
            navigationRouter.navigateTo(Router.WatchMovie(movieDetailsModel?.link, movieDetailsModel?.type))
        }
    }

    companion object {

        private const val ARG_MOVIE_URL = "ARG_MOVIE_URL"

        @JvmStatic
        fun newInstance(movieUrl: String?): MovieDetailsFragment {
            val fragment = MovieDetailsFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_MOVIE_URL, movieUrl)
            }
            return fragment
        }
    }
}