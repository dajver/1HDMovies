package com.a1hd.movies.api.repository

import com.a1hd.movies.api.RestHttpClient
import com.a1hd.movies.etc.extensions.io
import org.jsoup.Jsoup
import javax.inject.Inject

class ParseJsonMovieDetailsRepository @Inject constructor(
    private val restHttpClient: RestHttpClient
) {

    // Seasons are loaded from the series page HTML (a.ss-item elements with data-id hashes)
    // Episodes are fetched via: https://1hd.art/ajax/ajax.php?episode={season-data-id-hash}
    suspend fun fetchDetails(url: String): MoviesDetailsDataModel = io {
        val baseUrl = if (url.startsWith("https://1hd")) url else "https://1hd.art"
        val linkToMovieDetails = if (url.startsWith("https://1hd")) url else "$baseUrl$url"
        val doc = Jsoup.parse(restHttpClient.get(linkToMovieDetails))
        val type = if (linkToMovieDetails.contains("movie")) MovieType.MOVIE else MovieType.TV_SHOW
        val movieDetails = doc.select("div.detail-elements")
        val thumbnail = movieDetails.select("img.film-thumbnail-img").attr("src")
        val title = movieDetails.select("h3.heading-xl").text()
        val quality = movieDetails.select("div.quality").text()
        val linkToWatch = movieDetails.select("div.div-buttons").select("a").attr("href")
        val description = movieDetails.select("div.description").text()
        val others = movieDetails.select("div.others")
        val cast = others.select("div.item-casts").select("div.item-body").text()
        val genre = others.select("div.item-genres").select("div.item-body").text()
        val ratingAndOther = others.select("div.item").select("div.item-body").eachText()
        val duration = ratingAndOther.getOrElse(2) { "" }
        val country = ratingAndOther.getOrElse(3) { "" }
        val imdb = ratingAndOther.getOrElse(4) { "" }
        val release = ratingAndOther.getOrElse(5) { "" }
        val production = ratingAndOther.getOrElse(6) { "" }

        val watchMovieLink = "$baseUrl$linkToWatch"
        val episodeId = extractEpisodeId(watchMovieLink)
        val watchMovieLinkWithEpisodeId = "$baseUrl$linkToWatch/$episodeId"
        val movieDetailsModel = if (type == MovieType.MOVIE) {
            MoviesDetailsDataModel(title, thumbnail, linkToWatch, linkToMovieDetails, watchMovieLinkWithEpisodeId, type, description, quality, cast, genre, duration, country, imdb, release, production)
        } else {
            MoviesDetailsDataModel(title, thumbnail, linkToWatch, linkToMovieDetails, watchMovieLinkWithEpisodeId, type, description, quality, cast, genre, duration, country, imdb, release, production, getSeasons(doc))
        }
        movieDetailsModel
    }

    private suspend fun extractEpisodeId(watchUrl: String): String? {
        val doc = Jsoup.parse(restHttpClient.get(watchUrl))
        val scriptTags = doc.getElementsByTag("script")
        for (scriptTag in scriptTags) {
            val scriptContent = scriptTag.data()
            if (scriptContent.contains("const movie =")) {
                val pattern = "episodeId: '([0-9]+)'".toRegex()
                val matchResult = pattern.find(scriptContent)
                return matchResult?.groups?.get(1)?.value // Return the episodeId if found
            }
        }
        return null
    }

    private suspend fun getSeasons(doc: org.jsoup.nodes.Document): MutableList<MovieSeasonDataModel> = io {
        try {
            val seasonElements = doc.select("div.is-seasons").select("a.ss-item")
            val seasonMutableList = mutableListOf<MovieSeasonDataModel>()
            seasonElements.forEach { element ->
                val seasonHash = element.attr("data-id")
                val seasonNumber = element.text().trim()
                seasonMutableList.add(MovieSeasonDataModel(seasonHash, seasonNumber, getEpisodes(seasonHash)))
            }
            seasonMutableList
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }

    private suspend fun getEpisodes(seasonHash: String): MutableList<MovieEpisodesDataModel> = io {
        try {
            val ajaxLink = "https://1hd.art/ajax/ajax.php?episode=$seasonHash"
            val getResponse = restHttpClient.get(ajaxLink)
            val doc = Jsoup.parse(getResponse)
            val episodeElements = doc.select("a.ep-item")
            val episodesMutableList = mutableListOf<MovieEpisodesDataModel>()
            episodeElements.forEach { element ->
                val episodeNumber = element.select("span.number").text().trim()
                val episodeName = element.select("span.name").text().trim()
                val href = element.attr("href")
                val link = if (href.startsWith("https://1hd")) href else "https://1hd.art$href"
                episodesMutableList.add(MovieEpisodesDataModel(episodeNumber, episodeName, link))
            }
            episodesMutableList
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }
}

data class MoviesDetailsDataModel(
    val name: String,
    val thumbnail: String,
    val linkToWatch: String,
    val linkToDetails: String,
    val watchMovieLinkWithEpisodeId: String,
    val type: MovieType,
    val description: String,
    val quality: String,
    val cast: String,
    val genre: String,
    val duration: String,
    val country: String,
    val imdb: String,
    val release: String,
    val production: String,
    val seasonsList: MutableList<MovieSeasonDataModel>? = mutableListOf()
) {
    var addedAt: Long? = null
}

data class MovieSeasonDataModel(
    val seasonId: String,
    val seasonNumber: String,
    val episodes: MutableList<MovieEpisodesDataModel>
) {
    var isSelected: Boolean = false
}

data class MovieEpisodesDataModel(
    val episodeNumber: String,
    val episodeName: String,
    val link: String
) {
    var isSelected: Boolean = false
}
