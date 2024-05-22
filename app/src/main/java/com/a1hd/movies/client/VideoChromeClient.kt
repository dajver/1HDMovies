package com.a1hd.movies.client

import android.annotation.SuppressLint
import android.app.ActionBar
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import com.a1hd.movies.R

const val WEB_VIEW_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36"

@SuppressLint("SetJavaScriptEnabled")
class VideoChromeClient(
    private var container: FrameLayout,
    private val webView: WebView
) : WebChromeClient() {

    @SuppressLint("InflateParams")
    private val videoProgressView: View = LayoutInflater.from(container.context).inflate(R.layout.view_video_loading, null)

    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null
    private var actionBar: ActionBar? = null
    private var defaultPoster: Bitmap? = null

    var onConsoleErrorMessage: (String) -> Unit = {}

    init {
        with(webView.settings) {
            userAgentString = WEB_VIEW_USER_AGENT
            javaScriptEnabled = true
            domStorageEnabled = true
            setSupportMultipleWindows(true)
            javaScriptCanOpenWindowsAutomatically = true
            mediaPlaybackRequiresUserGesture = false
        }
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
    }

    fun setFullScreenView(actionBar: ActionBar?, frameLayout: FrameLayout) {
        this.actionBar = actionBar
        this.container = frameLayout
    }

    override fun onShowCustomView(view: View, callback: CustomViewCallback) {
        Log.d("IMG", "on show custom view, hide webview")
        webView.visibility = View.GONE

        if (customView != null) {
            callback.onCustomViewHidden()
            return
        }

        container.addView(view)
        container.visibility = View.VISIBLE
        customView = view
        customViewCallback = callback
        actionBar?.hide()
    }

    override fun onHideCustomView() {
        if (customView == null) return

        customView?.visibility = View.GONE
        container.removeView(customView)
        customView = null
        container.visibility = View.GONE
        customViewCallback?.onCustomViewHidden()

        webView.visibility = View.VISIBLE
        webView.goBack()

        actionBar?.show()
    }

    override fun getVideoLoadingProgressView(): View {
        return videoProgressView
    }

    override fun getDefaultVideoPoster(): Bitmap? {
        defaultPoster = super.getDefaultVideoPoster()
        return defaultPoster
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        val errorMessage = consoleMessage?.message().toString()
        if (errorMessage.contains("102630")) {
            onConsoleErrorMessage.invoke(errorMessage)
        }
        return super.onConsoleMessage(consoleMessage)
    }

    override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
        callback.invoke(origin, true, false)
    }
}