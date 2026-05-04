package com.a1hd.movies.api

import android.util.Log
import com.a1hd.movies.client.WEB_VIEW_USER_AGENT
import com.a1hd.movies.etc.extensions.io
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import org.conscrypt.Conscrypt
import java.io.IOException
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class RestHttpClient @Inject constructor() {

    private val client: OkHttpClient

    init {
        val sslContext = SSLContext.getInstance("TLS", Conscrypt.newProvider())
        sslContext.init(null, null, null)

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManager = trustManagerFactory.trustManagers[0] as X509TrustManager

        client = OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT))
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    suspend fun get(url: String): String = io {
        Log.d("RestHttpClient", "Connecting to URL: $url")

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", WEB_VIEW_USER_AGENT)
            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .header("Accept-Language", "en-US,en;q=0.5")
            .get()
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            response.body?.string() ?: throw IOException("Empty response body")
        } else {
            throw IOException("HTTP error code: ${response.code}")
        }
    }
}
