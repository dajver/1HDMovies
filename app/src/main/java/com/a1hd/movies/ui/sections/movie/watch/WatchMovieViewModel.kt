package com.a1hd.movies.ui.sections.movie.watch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a1hd.movies.api.RestHttpClient
import com.a1hd.movies.etc.extensions.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import org.jsoup.Jsoup
import javax.inject.Inject

@HiltViewModel
class WatchMovieViewModel @Inject constructor(
    private val restHttpClient: RestHttpClient
) : ViewModel() {

    private val embedUrlMutableLiveData = MutableLiveData<EmbedUrlResult>()
    val embedUrlLiveData: LiveData<EmbedUrlResult> = embedUrlMutableLiveData

    var embedUrl: String? = null
        private set

    fun fetchEmbedUrl(watchUrl: String) = launch {
        try {
            val html = restHttpClient.get(watchUrl)
            val plUrlPattern = "const pl_url = '([^']+)'".toRegex()
            val plUrlMatch = plUrlPattern.find(html)
            val plUrl = plUrlMatch?.groups?.get(1)?.value

            if (plUrl != null) {
                val serverHtml = restHttpClient.get(plUrl)
                val doc = Jsoup.parse(serverHtml)
                val firstServer = doc.select("a.sv-item").firstOrNull()
                val url = firstServer?.attr("data-id")
                embedUrl = url
                embedUrlMutableLiveData.postValue(EmbedUrlResult(url))
            } else {
                embedUrlMutableLiveData.postValue(EmbedUrlResult(null))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            embedUrlMutableLiveData.postValue(EmbedUrlResult(null))
        }
    }

    data class EmbedUrlResult(val url: String?)
}
