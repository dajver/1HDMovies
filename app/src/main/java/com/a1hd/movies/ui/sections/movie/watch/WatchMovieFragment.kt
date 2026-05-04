package com.a1hd.movies.ui.sections.movie.watch

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import com.a1hd.movies.api.RestHttpClient
import com.a1hd.movies.databinding.FragmentWatchMovieBinding
import com.a1hd.movies.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import javax.inject.Inject

@AndroidEntryPoint
class WatchMovieFragment: BaseFragment<FragmentWatchMovieBinding>(FragmentWatchMovieBinding::inflate) {

    @Inject
    lateinit var restHttpClient: RestHttpClient

    var movieUrl: String? = null
    private var embedUrl: String? = null

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
        binding.webView.setFullScreenView(requireActivity().actionBar, binding.fullscreenView)
        binding.webView.sourcesListLiveData.observe(viewLifecycleOwner) {
            binding.webView.ivSourceAvailable.isVisible = it.isNotEmpty()

            val referer = embedUrl ?: movieUrl ?: "https://1hd.art/"
            startActivity(VideoPlayerActivity.setUrl(requireContext(), it.first(), referer))
            navigationRouter.navigateBack()
        }

        binding.webView.sourcesLisFetchingLiveData.observe(viewLifecycleOwner) {
            binding.pbLoadingSources.isVisible = it
        }

        binding.webView.sourcesLoadingStatusLiveData.observe(viewLifecycleOwner) {
            binding.tvLoadingStatus.text = it
        }

        loadEmbedUrl()
    }

    private fun loadEmbedUrl() {
        binding.pbLoadingSources.isVisible = true
        lifecycleScope.launch {
            try {
                val url = fetchEmbedUrl(movieUrl!!)
                embedUrl = url
                if (url != null) {
                    binding.pbLoadingSources.isVisible = false
                    binding.webView.loadUrl(url)
                } else {
                    binding.pbLoadingSources.isVisible = false
                    binding.webView.loadUrl(movieUrl!!)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.pbLoadingSources.isVisible = false
                binding.webView.loadUrl(movieUrl!!)
            }
        }
    }

    private suspend fun fetchEmbedUrl(watchUrl: String): String? {
        try {
            val html = restHttpClient.get(watchUrl)
            // Extract pl_url from the page's inline script
            val plUrlPattern = "const pl_url = '([^']+)'".toRegex()
            val plUrlMatch = plUrlPattern.find(html)
            val plUrl = plUrlMatch?.groups?.get(1)?.value ?: return null

            // Fetch the server list from pl_url
            val serverHtml = restHttpClient.get(plUrl)
            val doc = Jsoup.parse(serverHtml)
            // Get the first server's embed URL (data-id attribute)
            val firstServer = doc.select("a.sv-item").firstOrNull()
            return firstServer?.attr("data-id")
        } catch (e: Exception) {
            e.printStackTrace()
            return null
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
