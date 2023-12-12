package com.a1hd.movies

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.a1hd.movies.views.VideoWebview

class MainActivity : AppCompatActivity() {

    private lateinit var webView: VideoWebview

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        webView.setFullScreenView(actionBar, findViewById(R.id.fullscreen_view))
        webView.loadUrl("https://1hd.to/home")

        onBackButtonPressed()
    }

    private fun AppCompatActivity.onBackButtonPressed() {
        val onBackPressed: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()){
                    webView.goBack()
                } else {
                    backClickListener {
                        finishAndRemoveTask()
                    }
                }
            }
        }
        this.onBackPressedDispatcher.addCallback(this, onBackPressed)
    }

    private var doubleBackToExitPressedOnce = false

    private fun backClickListener(onFinishCalled: () -> Unit) {
        val toast = Toast.makeText(this, getString(R.string.back_button_double_click_text), Toast.LENGTH_SHORT)

        if (doubleBackToExitPressedOnce) {
            toast.cancel()
            onFinishCalled.invoke()
            return
        }

        toast.show()
        doubleBackToExitPressedOnce = true
        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}