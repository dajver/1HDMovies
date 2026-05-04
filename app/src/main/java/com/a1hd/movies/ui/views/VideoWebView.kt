package com.a1hd.movies.ui.views

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.a1hd.movies.client.VideoChromeClient
import com.a1hd.movies.client.WEB_VIEW_USER_AGENT
import com.a1hd.movies.databinding.ViewVideoWebviewBinding
import com.a1hd.movies.etc.LastOpenedScreenRepository
import com.a1hd.movies.ui.sections.select.SelectSourceSheetFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class VideoWebView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    @Inject
    lateinit var lastOpenedScreenRepository: LastOpenedScreenRepository

    private var viewBinding: ViewVideoWebviewBinding
    private var chromeClient: VideoChromeClient
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 2000
    private val sourceList = mutableListOf<String>()
    private var isRequesting = false

    private val sourcesListMutableLiveData = MutableLiveData<List<String>>()
    val sourcesListLiveData: LiveData<List<String>> = sourcesListMutableLiveData

    private val sourcesListFetchingMutableLiveData = MutableLiveData<Boolean>()
    val sourcesLisFetchingLiveData: LiveData<Boolean> = sourcesListFetchingMutableLiveData

    private val sourcesLoadingStatusMutableLiveData = MutableLiveData<String>()
    val sourcesLoadingStatusLiveData: LiveData<String> = sourcesLoadingStatusMutableLiveData

    init {
        viewBinding = ViewVideoWebviewBinding.inflate(LayoutInflater.from(context), this, true)
        chromeClient = VideoChromeClient(viewBinding.videoViewFrame, viewBinding.videoViewWebview)
    }

    fun init() {
        viewBinding.videoViewWebview.webChromeClient = chromeClient
        viewBinding.videoViewWebview.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                val url = request.url.toString()
                if (url.contains(".m3u8")) {
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
                        }
                        isRequesting = false
                        sourcesListFetchingMutableLiveData.postValue(false)
                    }, delayMillis)
                }

                return super.shouldInterceptRequest(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Auto-click play button after page loads with multiple attempts
                val autoPlayScript = """
                    (function() {
                        function tryPlay() {
                            // JW Player
                            if (typeof jwplayer !== 'undefined') {
                                try { jwplayer().play(); return true; } catch(e) {}
                            }
                            // Video.js
                            var vjsPlayer = document.querySelector('.video-js');
                            if (vjsPlayer && vjsPlayer.player) {
                                try { vjsPlayer.player.play(); return true; } catch(e) {}
                            }
                            // HTML5 video
                            var video = document.querySelector('video');
                            if (video) {
                                video.play();
                                return true;
                            }
                            // Click play buttons
                            var selectors = ['.jw-icon-playback', '.jw-display-icon-container', '.vjs-big-play-button', '[class*="play-btn"]', '[class*="playBtn"]', 'button[aria-label*="Play"]', '.play-button', '#play-btn'];
                            for (var i = 0; i < selectors.length; i++) {
                                var btn = document.querySelector(selectors[i]);
                                if (btn) { btn.click(); return true; }
                            }
                            return false;
                        }
                        // Try multiple times with delays
                        setTimeout(tryPlay, 1000);
                        setTimeout(tryPlay, 3000);
                        setTimeout(tryPlay, 5000);
                    })();
                """.trimIndent()
                handler.postDelayed({
                    view?.evaluateJavascript(autoPlayScript, null)
                }, 2000)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                lastOpenedScreenRepository.lastOpenedPage = url
                return false
            }
        }
        viewBinding.videoViewWebview.settings.userAgentString = WEB_VIEW_USER_AGENT
        viewBinding.videoViewWebview.settings.mediaPlaybackRequiresUserGesture = false
        viewBinding.videoViewWebview.setBackgroundColor(Color.TRANSPARENT)
        viewBinding.videoViewWebview.settings.apply {
            cacheMode = WebSettings.LOAD_NO_CACHE
            javaScriptEnabled = true
            domStorageEnabled = true
            allowContentAccess = true
            javaScriptCanOpenWindowsAutomatically = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        chromeClient.onConsoleErrorMessage = {
            sourcesLoadingStatusMutableLiveData.postValue(it)
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
        lastOpenedScreenRepository.lastOpenedPage = url
    }

    fun canGoBack(): Boolean = viewBinding.videoViewWebview.canGoBack()

    fun goBack() {
        viewBinding.videoViewWebview.goBack()
    }

    val ivSourceAvailable
        get() = viewBinding.ivSourceAvailable
}