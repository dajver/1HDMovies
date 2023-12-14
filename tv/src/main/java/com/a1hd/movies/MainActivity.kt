package com.a1hd.movies

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.a1hd.movies.base.BaseActivity
import com.a1hd.movies.databinding.ActivityMainBinding
import com.a1hd.movies.etc.LastOpenedScreenRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var lastOpenedScreenRepository: LastOpenedScreenRepository

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.webView.init()
        binding.webView.setFullScreenView(actionBar, binding.fullscreenView)
        binding.webView.loadUrl("https://1hd.to/home")

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        binding.webView.sourcesListLiveData.observe(this) {
            binding.webView.ivSourceAvailable.isVisible = it.isNotEmpty()
        }
        binding.webView.sourcesLisFetchingLiveData.observe(this) {
            binding.pbLoadingSources.isVisible = it
        }
    }

    private fun setupListeners() {
        binding.webView.ivSourceAvailable.setOnClickListener {
            binding.webView.showSourceDialog(supportFragmentManager)
        }

        onBackButtonPressed()
    }

    private fun AppCompatActivity.onBackButtonPressed() {
        val onBackPressed: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
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
        val toast = Toast.makeText(
            this,
            getString(R.string.back_button_double_click_text),
            Toast.LENGTH_SHORT
        )

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