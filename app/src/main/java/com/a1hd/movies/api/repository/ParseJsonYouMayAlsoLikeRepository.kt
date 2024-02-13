package com.a1hd.movies.api.repository

import com.a1hd.movies.etc.extensions.io
import org.jsoup.Jsoup
import javax.inject.Inject

class ParseJsonYouMayAlsoLikeRepository @Inject constructor() {

    suspend fun fetchYouMayAlsoLike(url: String): List<MoviesDataModel> = io {
        val linkToMovie = if (url.startsWith("https://1hd")) url else "https://1hd.to/$url"
        val doc = Jsoup.connect(linkToMovie).get()
        val moviesElements = doc.select("div.swiper-slide").select("div.item-film")
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