package com.a1hd.movies.ui.sections.movie.watch

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.media3.common.util.UnstableApi
import com.a1hd.movies.databinding.FragmentWatchMovieBinding
import com.a1hd.movies.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchMovieFragment: BaseFragment<FragmentWatchMovieBinding>(FragmentWatchMovieBinding::inflate) {

    var movieUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieUrl = it.getString(ARG_MOVIE_URL, "")
        }
    }

    @UnstableApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (movieUrl.isNullOrEmpty()) {
            throw RuntimeException("movieUrl mustn't be null or empty")
        }

        binding.webView.init()
        binding.webView.loadUrl(movieUrl!!)
        binding.webView.setFullScreenView(requireActivity().actionBar, binding.fullscreenView)
        binding.webView.sourcesListLiveData.observe(viewLifecycleOwner) {
            binding.webView.ivSourceAvailable.isVisible = it.isNotEmpty()

            startActivity(VideoPlayerActivity.setUrl(requireContext(), it.first()))
            navigationRouter.navigateBack()
        }

        binding.webView.sourcesLisFetchingLiveData.observe(viewLifecycleOwner) {
            binding.pbLoadingSources.isVisible = it
        }

        binding.webView.sourcesLoadingStatusLiveData.observe(viewLifecycleOwner) {
            binding.tvLoadingStatus.text = it
            binding.webView.loadUrl(movieUrl!!)
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