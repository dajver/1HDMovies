package com.a1hd.movies.api.repository

import com.a1hd.movies.api.RestHttpClient
import com.a1hd.movies.etc.extensions.io
import org.jsoup.Jsoup
import javax.inject.Inject

class ParseJsonMoviesGenresRepository @Inject constructor(
    private val restHttpClient: RestHttpClient
) {

    suspend fun fetchMoviesByGenre(genre: GenresEnum, page: Int): List<MoviesDataModel> = io {
        val doc = Jsoup.parse(restHttpClient.get("${genre.url}?page=$page"))
        val moviesElements = doc.select("div.container").select("div.film-list").select("div.item-film")
        val filmVisualInformation = moviesElements.select("div.film-thumbnail").select("img.film-thumbnail-img")
        val filmTextInformation = moviesElements.select("div.film-detail")
        val filmReleaseInformation = moviesElements.select("div.film-info")
        val qualities = moviesElements.select("div.film-thumbnail").select("div.quality").textNodes()
        val thumbnails = filmVisualInformation.eachAttr("src")
        val names = filmVisualInformation.eachAttr("alt")
        val links = filmTextInformation.select("h3.film-name").select("a").eachAttr("href")
        val dateInfo = filmReleaseInformation.select("span.item").textNodes()
        val typeAndYear = mutableListOf<String>()
        for (i in 0 until dateInfo.size step 2) {
            val type = dateInfo.getOrNull(i)
            val year = dateInfo.getOrNull(i + 1)
            typeAndYear.add("$type,$year")
        }
        val moviesMap = mutableListOf<MoviesDataModel>()
        thumbnails.forEachIndexed { index, _ ->
            val name = names[index]
            val thumbnail = thumbnails[index]
            val link = links[index]
            val type = if (typeAndYear[index].split(",")[0] == "Movie") MovieType.MOVIE else MovieType.TV_SHOW
            val quality = if (qualities.size > index) qualities[index].text() else ""
            val releaseYear = if (typeAndYear.size > index) typeAndYear[index].split(",")[1] else ""
            moviesMap.add(MoviesDataModel(name, thumbnail, link, type, quality, releaseYear))
        }

        val firstSelectedItem = moviesMap.firstOrNull()
        if (firstSelectedItem != null) {
            firstSelectedItem.isSelected = true
            moviesMap[0] = firstSelectedItem
        }
        moviesMap
    }
}

enum class GenresEnum(val url: String) {
    ACTION("https://1hd.art/genre/action"),
    COMEDY("https://1hd.art/genre/comedy"),
    DRAMA("https://1hd.art/genre/drama"),
    FANTASY("https://1hd.art/genre/fantasy"),
    HORROR("https://1hd.art/genre/horror"),
    MYSTERY("https://1hd.art/genre/mystery"),
    TOP_IMDB("https://1hd.art/top-imdb");
}
