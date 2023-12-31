package com.a1hd.movies.ui.sections.movie.watch

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.media3.common.util.UnstableApi
import com.a1hd.movies.databinding.FragmentWatchMovieBinding
import com.a1hd.movies.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@UnstableApi
@AndroidEntryPoint
class WatchMovieFragment: BaseFragment<FragmentWatchMovieBinding>(FragmentWatchMovieBinding::inflate) {

    var movieUrl: String? = null

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

        val linkToMovie = if (movieUrl?.startsWith("https://1hd") == true) movieUrl else "https://1hd.to/$movieUrl"
        binding.webView.init()
        binding.webView.loadUrl(linkToMovie!!)
        binding.webView.setFullScreenView(requireActivity().actionBar, binding.fullscreenView)
        binding.webView.sourcesListLiveData.observe(viewLifecycleOwner) {
            binding.webView.ivSourceAvailable.isVisible = it.isNotEmpty()

            startActivity(VideoPlayerActivity.setUrl(requireContext(), it.first()))
            navigationRouter.navigateBack()
        }

        binding.webView.sourcesLisFetchingLiveData.observe(viewLifecycleOwner) {
            binding.pbLoadingSources.isVisible = it
        }
    }

    companion object {

        private const val ARG_MOVIE_URL = "ARG_MOVIE_URL"

        @JvmStatic
        fun newInstance(movieUrl: String?): WatchMovieFragment {
            val fragment = WatchMovieFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_MOVIE_URL, movieUrl)
            }
            return fragment
        }
    }
}