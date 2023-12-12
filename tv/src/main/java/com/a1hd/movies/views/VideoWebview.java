package com.a1hd.movies.views;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.a1hd.movies.R;
import com.a1hd.movies.client.VideoChromeClient;

import java.util.Map;

/**
 * Created by ravindu on 29/06/17.
 * <p>
 * A Webview wrapped in a FrameLayout that supports full screen html5
 * video playback out of the box.
 */
public class VideoWebview extends FrameLayout {
    private WebView webView;
    private FrameLayout frameLayout;
    private VideoChromeClient chromeClient;


    public VideoWebview(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VideoWebview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.rw_video_webview, this);

        frameLayout = findViewById(R.id.video_view_frame);
        webView = findViewById(R.id.video_view_webview);

        chromeClient = new VideoChromeClient(context, frameLayout, webView);
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(new WebViewClient());
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.endsWith(".m3u8")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
                return super.shouldInterceptRequest(view, request);
            }
        });
    }

    /**
     * get the internal webview
     *
     * @return underlying webview object
     */
    public WebView getWebView() {
        return webView;
    }

    /**
     * Sets an external view as the full screen playback view.
     * If the actionBar object is provided, it will be hidden in fullscreen mode
     *
     * @param actionBar   actionbar of the calling activity
     * @param frameLayout external fullscreen view
     */
    public void setFullScreenView(@Nullable ActionBar actionBar, FrameLayout frameLayout) {
        this.frameLayout = frameLayout;

        chromeClient.setFullScreenView(actionBar, frameLayout);
    }

    /**
     * {@link WebView#loadUrl(String)}
     */
    public void loadUrl(String url) {
        webView.loadUrl(url);
    }

    /**
     * {@link WebView#loadUrl(String, Map)}
     */
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        webView.loadUrl(url, additionalHttpHeaders);
    }

    /**
     * {@link WebView#loadData(String, String, String)}
     */
    public void loadData(String data, String mimeType, String encoding) {
        webView.loadData(data, mimeType, encoding);
    }

    /**
     * {@link WebView#loadDataWithBaseURL(String, String, String, String, String)}
     */
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    /**
     * Warning : do not set 'setJavaScriptEnabled(false)' as it will
     * break video playback
     *
     * @return settings of the underlying webview
     */
    public WebSettings getSettings() {
        return webView.getSettings();
    }

    /**
     * {@link WebView#canGoBack()}
     */
    public boolean canGoBack() {
        return webView.canGoBack();
    }

    /**
     * {@link WebView#canGoForward()}
     */
    public boolean canGoForward() {
        return webView.canGoForward();
    }

    /**
     * {@link WebView#canGoBackOrForward(int)}
     */
    public boolean canGoBackOrForward(int steps) {
        return webView.canGoBackOrForward(steps);
    }

    /**
     * {@link WebView#goBack()}
     */
    public void goBack() {
        webView.goBack();
    }

    /**
     * {@link WebView#goForward()}
     */
    public void goForward() {
        webView.goForward();
    }

    /**
     * {@link WebView#goBackOrForward(int)}
     */
    public void goBackOrForward(int steps) {
        webView.goBackOrForward(steps);
    }

    /**
     * {@link WebView#setWebViewClient(WebViewClient)}
     */
    public void setWebViewClient(WebViewClient client) {
        webView.setWebViewClient(client);
    }


}