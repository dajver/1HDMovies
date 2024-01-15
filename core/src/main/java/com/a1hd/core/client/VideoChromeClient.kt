package com.a1hd.core.client

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.app.ActionBar
import android.util.Log
import android.widget.FrameLayout
import com.a1hd.core.R

class VideoChromeClient(
    private var container: FrameLayout,
    private val webView: WebView
) : WebChromeClient() {

    private val videoProgressView: View = LayoutInflater.from(container.context).inflate(R.layout.view_video_loading, null)

    private var customView: View? = null
    private var customViewCallback: CustomViewCallback? = null
    private var actionBar: ActionBar? = null
    private var defaultPoster: Bitmap? = null

    fun setFullScreenView(actionBar: ActionBar?, frameLayout: FrameLayout) {
        this.actionBar = actionBar
        this.container = frameLayout
    }

    override fun onShowCustomView(view: View, callback: CustomViewCallback) {
        Log.d("IMG", "on show custom view, hide webview")
        webView.visibility = View.GONE

        // if a view already exists then immediately terminate the new one
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

        // Hide the custom view.
        customView?.visibility = View.GONE

        // Remove the custom view from its container.
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

    override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
        callback.invoke(origin, true, false)
    }
}