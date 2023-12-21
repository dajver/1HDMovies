package com.a1hd.movies.ui.repository

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
        val thumbnails = filmVisualInformation.eachAttr("src")
        val names = filmVisualInformation.eachAttr("alt")
        val links = filmTextInformation.select("h3.film-name").select("a").eachAttr("href")
        val dateInfo = filmReleaseInformation.select("span.item").textNodes()
        val type = dateInfo.filter { it.text().contains("Movie") || it.text().contains("Series") }
        val moviesMap = mutableListOf<MoviesDataModel>()
        thumbnails.forEachIndexed { index, data ->
            val name = names[index]
            val thumbnail = thumbnails[index]
            val link = links[index]
            val type = if (type[index].text() == "Movie") MovieType.MOVIE else MovieType.TV_SHOW
            moviesMap.add(MoviesDataModel(name, thumbnail, link, type))
        }
        moviesMap
    }
}

data class MoviesDataModel(
    val name: String,
    val thumbnail: String,
    val link: String,
    val type: MovieType
)

enum class MovieType {
    MOVIE, TV_SHOW
}