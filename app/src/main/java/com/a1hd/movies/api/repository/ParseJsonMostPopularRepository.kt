package com.a1hd.movies.api.repository

import com.a1hd.movies.api.RestHttpClient
import com.a1hd.movies.etc.extensions.io
import org.jsoup.Jsoup
import javax.inject.Inject

class ParseJsonMostPopularRepository @Inject constructor(
    private val restHttpClient: RestHttpClient
) {

    suspend fun fetchMostPopular(): List<MostPopularMoviesDataModel> = io {
        val doc = Jsoup.parse(restHttpClient.get("https://1hd.art/home"))
        val moviesElements = doc.select("div.swiper-wrapper")
        val movieDetailsContainer = moviesElements.select("div.container").select("div.is-caption")
        val thumbnails = moviesElements.select("div.slide-cover").select("img").eachAttr("src")
        val qualityAndYear = movieDetailsContainer.select("span.item").textNodes()
        val qualities = mutableListOf<String>()
        for (i in 0 until qualityAndYear.size step 3) {
            val quality = qualityAndYear.getOrNull(i)
            val type = qualityAndYear.getOrNull(i + 1)
            val year = qualityAndYear.getOrNull(i + 2)
            qualities.add("$quality, $type, $year")
        }
        val names = movieDetailsContainer.select("a").eachAttr("title")
        val descriptions = movieDetailsContainer.select("p.description").textNodes()
        val links = movieDetailsContainer.select("div.div-buttons").select("a").eachAttr("href")
        val moviesMap = mutableListOf<MostPopularMoviesDataModel>()
        thumbnails.forEachIndexed { index, _ ->
            val name = names[index]
            val thumbnail = thumbnails[index]
            val link = links[index]
            val watchMovieLink = if (link.startsWith("https://1hd")) link else "https://1hd.art$link"
            val description = descriptions[index].text()
            val quality = qualities[index]
            moviesMap.add(MostPopularMoviesDataModel(name, thumbnail, watchMovieLink, quality, description))
        }

        moviesMap
    }

}

data class MostPopularMoviesDataModel(
    val name: String,
    val thumbnail: String,
    val link: String,
    val quality: String,
    val description: String
)
