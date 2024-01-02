package com.a1hd.movies.api

import com.a1hd.movies.etc.extensions.io
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpStatus
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.StatusLine
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ssl.NoopHostnameVerifier
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.conn.ssl.SSLConnectionSocketFactory
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.HttpClients
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class RestHttpClient @Inject constructor() {

    suspend fun get(url: String): String = io {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            override fun checkClientTrusted(chain: Array<out X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>, authType: String) {}
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
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