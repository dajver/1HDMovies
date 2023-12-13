package com.a1hd.movies.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.app.ActionBar
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.a1hd.movies.client.VideoChromeClient
import com.a1hd.movies.databinding.RwVideoWebviewBinding
import com.a1hd.movies.select.SelectSourceSheetFragment

class VideoWebView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    private var viewBinding: RwVideoWebviewBinding
    private var chromeClient: VideoChromeClient
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 1000

    init {
        viewBinding = RwVideoWebviewBinding.inflate(LayoutInflater.from(context), this, true)
        chromeClient = VideoChromeClient(viewBinding.videoViewFrame, viewBinding.videoViewWebview)
    }

    fun init(fragmentManager: FragmentManager) {
        val sourceList = mutableListOf<String>()
        viewBinding.videoViewWebview.webChromeClient = chromeClient
        viewBinding.videoViewWebview.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                val url = request.url.toString()
                if (url.endsWith(".m3u8")) {
                    sourceList.add(url)
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({
                        if (sourceList.isNotEmpty()) {
                            showSourceDialog(fragmentManager, sourceList)
                        }
                    }, delayMillis)
                }
                return super.shouldInterceptRequest(view, request)
            }
        }
        viewBinding.videoViewWebview.settings.mediaPlaybackRequiresUserGesture = false
        viewBinding.videoViewWebview.setBackgroundColor(Color.TRANSPARENT)
        viewBinding.videoViewWebview.settings.apply {
            cacheMode = WebSettings.LOAD_NO_CACHE
            javaScriptEnabled = true
        }
    }

    private fun showSourceDialog(fragmentManager: FragmentManager, sourceList: MutableList<String>) {
        val sourceDialog = SelectSourceSheetFragment()
        sourceDialog.setSourceList(sourceList)
        sourceDialog.show(fragmentManager, "SelectSourceSheetFragment")
        sourceDialog.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_DESTROY) {
                sourceList.clear()
            }
        })
    }

    fun setFullScreenView(actionBar: ActionBar?, frameLayout: FrameLayout) {
        chromeClient.setFullScreenView(actionBar, frameLayout)
    }

    fun loadUrl(url: String) {
        viewBinding.videoViewWebview.loadUrl(url)
    }

    fun canGoBack(): Boolean = viewBinding.videoViewWebview.canGoBack()

    fun goBack() {
        viewBinding.videoViewWebview.goBack()
    }
}