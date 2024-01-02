package com.a1hd.movies.ui.repository

import android.util.Log
import com.a1hd.movies.etc.extensions.io
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpStatus
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.StatusLine
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ssl.NoopHostnameVerifier
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ssl.SSLConnectionSocketFactory
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClients
import org.jsoup.Jsoup
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class ParseJsonSearchRepository @Inject constructor() {

    suspend fun fetchSearchResult(keyword: String): List<MoviesDataModel> = io {
        val getResponse = get("https://1hd.to/search?keyword=$keyword")
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
            val type = if (typeAndYear[index] == "Movie") MovieType.MOVIE else MovieType.TV_SHOW
            val quality = if (index >= qualities.size) "" else qualities[index].text()
            moviesMap.add(MoviesDataModel(name, thumbnail, link, type, quality))
        }
        moviesMap
    }

    private suspend fun get(url: String): String = io {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String) {}
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        val sslSocketFactory = sslContext.socketFactory
        val httpclient = HttpClients.custom().setSSLSocketFactory(
            SSLConnectionSocketFactory(sslSocketFactory, NoopHostnameVerifier.INSTANCE)
        ).build()
        val response: HttpResponse = httpclient.execute(HttpGet(url))
        val statusLine: StatusLine = response.statusLine
        if (statusLine.statusCode === HttpStatus.SC_OK) {
            val out = ByteArrayOutputStream()
            response.entity.writeTo(out)
            return@io out.toString()
        } else {
            response.entity.content.close()
            throw IOException(statusLine.reasonPhrase)
        }
    }
}