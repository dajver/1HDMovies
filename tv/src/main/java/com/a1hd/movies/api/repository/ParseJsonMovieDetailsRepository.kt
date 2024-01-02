package com.a1hd.movies.api.repository

import com.a1hd.movies.api.RestHttpClient
import com.a1hd.movies.etc.extensions.io
import org.jsoup.Jsoup
import javax.inject.Inject

class ParseJsonMovieDetailsRepository @Inject constructor(
    private val restHttpClient: RestHttpClient
) {

    // EXAMPLE
    // Doctor WHO
    // https://1hd.to/series/watch-doctor-who-online-39521 - link to TV Show on the web page
    // https://1hd.sx/ajax/movie/seasons/39521 - 39521 is ID of the link to TV Show seasons
    // https://1hd.sx/ajax/movie/season/episodes/250 - 250 is ID for the season from previous request for /seasons
    suspend fun fetchDetails(url: String): MoviesDetailsDataModel = io {
        val linkToMovie = if (url.startsWith("https://1hd")) url else "https://1hd.to/$url"
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
        val movieDetailsModel = if (type == MovieType.MOVIE){
            MoviesDetailsDataModel(title, thumbnail, link, type, description, quality, cast, genre, duration, country, imdb, release, production)
        } else {
            MoviesDetailsDataModel(title, thumbnail, link, type, description, quality, cast, genre, duration, country, imdb, release, production, getSeasons(linkToMovie))
        }
        movieDetailsModel
    }

    private suspend fun getSeasons(linkToMovie: String): MutableList<MovieSeasonDataModel> = io {
        val regex = Regex("[0-9]+$")
        val match = regex.find(linkToMovie)
        val movieId = match?.value
        val ajaxLink = "https://1hd.sx/ajax/movie/seasons/$movieId"
        val getResponse = restHttpClient.get(ajaxLink)
        val doc = Jsoup.parse(getResponse)
        val seasonIdList = doc.select("div.is-seasons").select("a").eachAttr("data-id")
        val seasonNumberList = doc.select("div.is-seasons").select("strong").textNodes()
        val seasonMutableList = mutableListOf<MovieSeasonDataModel>()
        seasonIdList.forEachIndexed  { index, data ->
            val seasonId = seasonIdList[index]
            val seasonNumber = seasonNumberList[index].text()
            seasonMutableList.add(MovieSeasonDataModel(seasonId, seasonNumber, getEpisodes(seasonId)))
        }
        seasonMutableList
    }

    private suspend fun getEpisodes(seasonId: String): MutableList<MovieEpisodesDataModel> = io {
        val ajaxLink = "https://1hd.sx/ajax/movie/season/episodes/$seasonId"
        val getResponse = restHttpClient.get(ajaxLink)
        val doc = Jsoup.parse(getResponse)
        val episodeNumberList = doc.select("div.is-info").select("span.number").textNodes()
        val episodeNameList = doc.select("div.is-info").select("span.name").textNodes()
        val linkList = doc.select("a").eachAttr("href")
        val episodesMutableList = mutableListOf<MovieEpisodesDataModel>()
        episodeNumberList.forEachIndexed { index, _ ->
            val episodeNumber = episodeNumberList[index].text()
            val episodeName = episodeNameList[index].text()
            val link = linkList[index]
            episodesMutableList.add(MovieEpisodesDataModel(episodeNumber, episodeName, link))
        }
        episodesMutableList
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
    val production: String,
    val seasonsList: MutableList<MovieSeasonDataModel>? = mutableListOf()
)

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