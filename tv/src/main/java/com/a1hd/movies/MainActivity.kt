package com.a1hd.movies

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.a1hd.movies.base.BaseActivity
import com.a1hd.movies.databinding.ActivityMainBinding
import com.a1hd.movies.ui.navigation.NavigationRouter
import com.a1hd.movies.ui.navigation.route.Router
import dagger.hilt.android.AndroidEntryPoint
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


private const val SPLASH_DISPLAY_LENGTH = 2000

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var navigationRouter: NavigationRouter

    private val handler = Handler(Looper.getMainLooper())

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableSSLCheck()
        navigationRouter.init(supportFragmentManager)
        navigationRouter.navigateTo(Router.Splash)
        handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH.toLong())
        onBackButtonPressed()
    }

    private val runnable = Runnable {
        navigationRouter.navigateTo(Router.Dashboard)
    }

    private fun AppCompatActivity.onBackButtonPressed() {
        val onBackPressed: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navigationRouter.onBackPressed()) {
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

    @SuppressLint("TrustAllX509TrustManager", "CustomX509TrustManager")
    private fun disableSSLCheck() {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}

            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        })
        try {
            val sc = SSLContext.getInstance("SSL")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        } catch (e: Exception) {
            // ignore
        }
        val allHostsValid = HostnameVerifier { _, _ -> true }
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
    }
}