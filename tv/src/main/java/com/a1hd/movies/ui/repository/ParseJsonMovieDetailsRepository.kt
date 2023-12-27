package com.a1hd.movies.ui.repository

import com.a1hd.movies.etc.extensions.io
import org.jsoup.Jsoup
import javax.inject.Inject

class ParseJsonMovieDetailsRepository @Inject constructor() {

    suspend fun fetchDetails(url: String): MoviesDetailsDataModel = io {
        val linkToMovie = if (url.startsWith("https://1hd")) url else "https://1hd.to/$url"
//doc.select("div.film-episodes").select("div.film-episodes").textNodes()
        val doc = Jsoup.connect(linkToMovie).get()
        val type = if (linkToMovie.contains("movie")) MovieType.MOVIE else MovieType.TV_SHOW
        val movieDetails = doc.select("div.detail-elements")
        val thumbnail = movieDetails.select("img.film-thumbnail-img").attr("src")
        val title = movieDetails.select("h3.heading-xl").text()
        val quality = movieDetails.select("div.quality").text()
        val link = movieDetails.select("div.div-buttons").select("a").attr("href")
        val description = movieDetails.select("div.description").text()
        val others = movieDetails.select("div.others")
        val cast = others.select("div.item-casts").select("div.item-body").text()
        val genre = others.select("div.item-genres").select("div.item-body").text()
        val ratingAndOther = others.select("div.item").select("div.item-body").eachText()
        val duration = if (ratingAndOther.size >= 2) ratingAndOther[2] else ""
        val country = if (ratingAndOther.size >= 3) ratingAndOther[3] else ""
        val imdb = if (ratingAndOther.size >= 4) ratingAndOther[4] else ""
        val release = if (ratingAndOther.size >= 5) ratingAndOther[5] else ""
        val production = if (ratingAndOther.size >= 6) ratingAndOther[6] else ""
        if (type == MovieType.TV_SHOW) {
            // TODO: add list to tv show episodes
        }
        val movieDetailsModel = MoviesDetailsDataModel(title, thumbnail, link, type, description, quality, cast, genre, duration, country, imdb, release, production)
        movieDetailsModel
    }
}

data class MoviesDetailsDataModel(
    val name: String,
    val thumbnail: String,
    val link: String,
    val type: MovieType,
    val description: String,
    val quality: String,
    val cast: String,
    val genre: String,
    val duration: String,
    val country: String,
    val imdb: String,
    val release: String,
    val production: String
)