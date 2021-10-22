package com.development.mywebsiteconvertintoapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.development.mywebsiteconvertintoapp.databinding.ActivityMainBinding
import com.google.android.gms.ads.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var progressbarVisibility = ObservableField(View.VISIBLE)
    var websiteUrl = "https://www.flipkart.com/"
    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this

        //Todo hide toolbar
        supportActionBar?.hide()

        //Todo Webview
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        binding.webView.loadUrl(websiteUrl)

        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progressbarVisibility.set(View.GONE)
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                progressbarVisibility.set(View.GONE)
                Toast.makeText(applicationContext, "No internet connection", Toast.LENGTH_LONG)
                    .show()
                binding.webView.loadUrl("file:///android_asset/lost.html")
            }
        }

        //Todo Admob banner Ad
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        showBannerAd()

        //Todo Swipe Down Refresh
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = true
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipe.isRefreshing = false
                binding.webView.loadUrl(websiteUrl)
            }, 3000)
        }
        binding.swipe.setColorSchemeColors(
            ContextCompat.getColor(this, android.R.color.holo_blue_bright),
            ContextCompat.getColor(this, android.R.color.holo_orange_dark),
            ContextCompat.getColor(this, android.R.color.holo_green_dark),
            ContextCompat.getColor(this, android.R.color.holo_red_dark)
        )
    }

    private fun showBannerAd() {
        binding.adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                val toastMessage = "ad loaded"
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_LONG).show()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                super.onAdFailedToLoad(adError)
                val toastMessage = "ad Failed"
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_LONG).show()
            }

            override fun onAdOpened() {
                super.onAdOpened()
                val toastMessage = "ad is open"
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_LONG).show()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                val toastMessage = "ad is clicked"
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_LONG).show()
            }

            override fun onAdClosed() {
                super.onAdClosed()
                val toastMessage = "ad is closed"
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_LONG).show()
            }

            override fun onAdImpression() {
                super.onAdImpression()
                val toastMessage = "ad impression"
                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPause() {
        if (binding.adView != null) {
            binding.adView.pause();
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (binding.adView != null) {
            binding.adView.resume();
        }
    }

    override fun onDestroy() {
        if (binding.adView != null) {
            binding.adView.destroy();
        }
        super.onDestroy();
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            if (backPressedOnce) {
                finishAffinity()
                return
            }
            backPressedOnce = true
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                backPressedOnce = false
            }, 1000)
        }
    }
    /* override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
       if (keyCode == KeyEvent.KEYCODE_BACK && binding.webView.canGoBack()) {
           binding.webView.goBack()
           return true
       }
       return super.onKeyDown(keyCode, event)
   }*/
}