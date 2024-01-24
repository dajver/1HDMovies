package com.a1hd.movies.api.repository

import com.a1hd.movies.etc.extensions.io
import org.jsoup.Jsoup
import javax.inject.Inject

class ParseJsonTvShowsRepository @Inject constructor() {

    suspend fun fetchTvShows(page: Int): List<MoviesDataModel> = io {
        val doc = Jsoup.connect("https://1hd.to/tv-series?page=$page").get()
        val moviesElements = doc.select("div.container").select("div.film-list").select("div.item-film")
        val filmVisualInformation = moviesElements.select("div.film-thumbnail").select("img.film-thumbnail-img")
        val filmTextInformation = moviesElements.select("div.film-detail")
        val filmReleaseInformation = moviesElements.select("div.film-info")
        val quality = moviesElements.select("div.film-thumbnail").select("div.quality").textNodes()
        val thumbnails = filmVisualInformation.eachAttr("src")
        val names = filmVisualInformation.eachAttr("alt")
        val links = filmTextInformation.select("h3.film-name").select("a").eachAttr("href")
        val dateInfo = filmReleaseInformation.select("span.item").textNodes()
        val episodes = dateInfo.filter { it.text().contains("SS") }
        val moviesMap = mutableListOf<MoviesDataModel>()
        thumbnails.forEachIndexed { index, _ ->
            val name = names[index]
            val thumbnail = thumbnails[index]
            val link = links[index]
            val type = MovieType.TV_SHOW
            val quality = quality[index].text()
            val episode = episodes[index].text()
            moviesMap.add(MoviesDataModel(name, thumbnail, link, type, quality, episode))
        }

        val firstSelectedItem = moviesMap.firstOrNull()
        if (firstSelectedItem != null) {
            firstSelectedItem.isSelected = true
            moviesMap[0] = firstSelectedItem
        }
        moviesMap
    }
}