package com.a1hd.core.api.repository

import com.a1hd.core.api.RestHttpClient
import com.a1hd.core.etc.extensions.io
import org.jsoup.Jsoup
import javax.inject.Inject

class ParseJsonSearchRepository @Inject constructor(
    private val restHttpClient: RestHttpClient
) {

    suspend fun fetchSearchResult(keyword: String): List<MoviesDataModel> = io {
        val getResponse = restHttpClient.get("https://1hd.to/search?keyword=$keyword")
        val doc = Jsoup.parse(getResponse)
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
            typeAndYear.add("$type, $year")
        }
        val moviesMap = mutableListOf<MoviesDataModel>()
        thumbnails.forEachIndexed { index, _ ->
            val name = names[index]
            val thumbnail = thumbnails[index]
            val link = links[index]
            val type = if (typeAndYear[index].split(",")[0] == "Movie") MovieType.MOVIE else MovieType.TV_SHOW
            val quality = if (index >= qualities.size) "" else qualities[index].text()
            val releaseYear = typeAndYear[index].split(",")[1]
            moviesMap.add(MoviesDataModel(name, thumbnail, link, type, quality, releaseYear))
        }
        moviesMap
    }
}