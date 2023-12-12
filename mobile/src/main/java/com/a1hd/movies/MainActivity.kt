package com.a1hd.movies

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://1hd.to/home")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.loadUrl("javascript:(function() { /* Your JS Code Here */ })()")
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()){
                webView.goBack()
            } else {
                finishAndRemoveTask()
            }
        }
    }
}

