package com.a1hd.movies.api

import android.util.Log
import com.a1hd.movies.etc.extensions.io
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class RestHttpClient @Inject constructor() {

    suspend fun get(url: String): String = io {
        Log.d("RestHttpClient", "Connecting to URL: $url")

        val urlConnection = URL(url).openConnection() as HttpURLConnection
        urlConnection.instanceFollowRedirects = true
        try {
            urlConnection.apply {
                requestMethod = "GET"
                connectTimeout = 15000
                readTimeout = 15000
            }

            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader(InputStreamReader(urlConnection.inputStream)).use { reader ->
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    return@io response.toString()
                }
            } else {
                throw IOException("HTTP error code: ${urlConnection.responseCode}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException(e.cause)
        } finally {
            urlConnection.disconnect()
        }
    }
}