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
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a1hd.movies.client.VideoChromeClient
import com.a1hd.movies.databinding.RwVideoWebviewBinding
import com.a1hd.movies.select.SelectSourceSheetFragment

class VideoWebView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    private var viewBinding: RwVideoWebviewBinding
    private var chromeClient: VideoChromeClient
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 1000
    private val sourceList = mutableListOf<String>()
    private var isRequesting = false

    private val sourcesListMutableLiveData = MutableLiveData<List<String>>()
    val sourcesListLiveData: LiveData<List<String>> = sourcesListMutableLiveData

    private val sourcesListFetchingMutableLiveData = MutableLiveData<Boolean>()
    val sourcesLisFetchingLiveData: LiveData<Boolean> = sourcesListFetchingMutableLiveData

    init {
        viewBinding = RwVideoWebviewBinding.inflate(LayoutInflater.from(context), this, true)
        chromeClient = VideoChromeClient(viewBinding.videoViewFrame, viewBinding.videoViewWebview)
    }

    fun init(fragmentManager: FragmentManager) {
        viewBinding.videoViewWebview.webChromeClient = chromeClient
        viewBinding.videoViewWebview.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                val url = request.url.toString()
                if (url.endsWith(".m3u8")) {
                    if (!isRequesting) {
                        sourceList.clear()
                        isRequesting = true
                        sourcesListFetchingMutableLiveData.postValue(true)
                    }

                    sourceList.add(url)
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed({
                        if (sourceList.isNotEmpty()) {
                            sourcesListMutableLiveData.postValue(sourceList)
                            showSourceDialog(fragmentManager)
                        }
                        isRequesting = false
                        sourcesListFetchingMutableLiveData.postValue(false)
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

    fun showSourceDialog(fragmentManager: FragmentManager) {
        val sourceDialog = SelectSourceSheetFragment()
        sourceDialog.setSourceList(sourceList)
        sourceDialog.show(fragmentManager, "SelectSourceSheetFragment")
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