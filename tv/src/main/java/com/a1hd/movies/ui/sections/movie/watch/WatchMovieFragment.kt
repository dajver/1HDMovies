package com.a1hd.movies.ui.sections.movie.watch

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.a1hd.movies.databinding.FragmentWatchMovieBinding
import com.a1hd.movies.ui.base.BaseFragment
import com.a1hd.movies.ui.repository.MovieType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchMovieFragment: BaseFragment<FragmentWatchMovieBinding>(FragmentWatchMovieBinding::inflate) {

    private var movieUrl: String? = null
    private var movieType: MovieType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieUrl = it.getString(ARG_MOVIE_URL, "")
            movieType = it.getSerializable(ARG_MOVIE_TYPE) as MovieType
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (movieUrl.isNullOrEmpty()) {
            throw RuntimeException("movieUrl mustn't be null or empty")
        }

        val linkToMovie = if (movieUrl?.startsWith("https://1hd") == true) movieUrl else "https://1hd.to/$movieUrl"
        binding.webView.init()
        binding.webView.loadUrl(linkToMovie!!)
        binding.webView.setFullScreenView(requireActivity().actionBar, binding.fullscreenView)
        binding.webView.sourcesListLiveData.observe(viewLifecycleOwner) {
            binding.webView.ivSourceAvailable.isVisible = it.isNotEmpty()
        }
        binding.webView.sourcesLisFetchingLiveData.observe(viewLifecycleOwner) {
            binding.pbLoadingSources.isVisible = it
        }

        binding.vBackground.isVisible = movieType == MovieType.MOVIE
    }

    companion object {

        private const val ARG_MOVIE_URL = "ARG_MOVIE_URL"
        private const val ARG_MOVIE_TYPE = "ARG_MOVIE_TYPE"

        @JvmStatic
        fun newInstance(movieUrl: String?, type: MovieType?): WatchMovieFragment {
            val fragment = WatchMovieFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_MOVIE_URL, movieUrl)
                putSerializable(ARG_MOVIE_TYPE, type)
            }
            return fragment
        }
    }
}