package com.a1hd.movies.api.repository

import com.a1hd.movies.etc.extensions.io
import org.jsoup.Jsoup
import javax.inject.Inject

class ParseJsonDashboardRepository @Inject constructor() {

    suspend fun fetchDashboard(): List<MoviesDataModel> = io {
        val doc = Jsoup.connect("https://1hd.to/home").get()
        val moviesElements = doc.select("div.container").select("div.film-list").select("div.item-film")
        val filmVisualInformation = moviesElements.select("div.film-thumbnail").select("img.film-thumbnail-img")
        val filmTextInformation = moviesElements.select("div.film-detail")
        val filmReleaseInformation = moviesElements.select("div.film-info")
        val qualities = moviesElements.select("div.film-thumbnail").select("div.quality").textNodes()
        val thumbnails = filmVisualInformation.eachAttr("src")
        val names = filmVisualInformation.eachAttr("alt")
        val links = filmTextInformation.select("h3.film-name").select("a").eachAttr("href")
        val dateInfo = filmReleaseInformation.select("span.item").textNodes()
        val episodesAndOther = mutableListOf<String>()
        for (i in 0 until dateInfo.size step 2) {
            val type = dateInfo.getOrNull(i)
            val yearAndEpisodes = dateInfo.getOrNull(i + 1)
            episodesAndOther.add("$type,$yearAndEpisodes")
        }
        val moviesMap = mutableListOf<MoviesDataModel>()
        thumbnails.forEachIndexed { index, _ ->
            val name = names[index]
            val thumbnail = thumbnails[index]
            val link = links[index]
            val type = if (episodesAndOther[index].split(",")[0] == "Movie") MovieType.MOVIE else MovieType.TV_SHOW
            val quality = qualities[index].text()
            val episode = episodesAndOther[index].split(",")[1]
            moviesMap.add(MoviesDataModel(name, thumbnail, link, type, quality, episode))
        }
        moviesMap
    }
}

data class MoviesDataModel(
    val name: String,
    val thumbnail: String,
    val link: String,
    val type: MovieType,
    val quality: String,
    val other: String
) {
    var genre: GenresEnum? = null
    var isSelected: Boolean = false
}

enum class MovieType {
    MOVIE, TV_SHOW
}