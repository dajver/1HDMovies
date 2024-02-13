package com.a1hd.movies.api.repository

import com.a1hd.movies.etc.extensions.io
import org.jsoup.Jsoup
import javax.inject.Inject

class ParseJsonMoviesGenresRepository @Inject constructor() {

    suspend fun fetchMoviesByGenre(genre: GenresEnum, page: Int): List<MoviesDataModel> = io {
        val doc = Jsoup.connect("${genre.url}?page=$page").get()
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
    ACTION("https://1hd.to/genre/action-10"),
    COMEDY("https://1hd.to/genre/comedy-7"),
    DRAMA("https://1hd.to/genre/drama-4"),
    FANTASY("https://1hd.to/genre/fantasy-13"),
    HORROR("https://1hd.to/genre/horror-14"),
    MYSTERY("https://1hd.to/genre/mystery-1"),
    TOP_IMDB("https://1hd.to/top-imdb");
}