package com.a1hd.movies.api

import com.a1hd.movies.etc.extensions.io
import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import org.apache.http.StatusLine
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RestHttpClient @Inject constructor() {

    suspend fun get(url: String): String = io {
        val urlConnection = URL(url).openConnection() as HttpURLConnection
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
        } finally {
            urlConnection.disconnect()
        }
    }
}