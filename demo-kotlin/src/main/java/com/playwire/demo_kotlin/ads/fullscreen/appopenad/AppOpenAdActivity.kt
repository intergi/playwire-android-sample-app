package com.playwire.demo_kotlin.ads.fullscreen.appopenad

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.appopen.PWAppOpenAd
import com.playwire.demo_kotlin.R
import com.playwire.demo_kotlin.misc.Constant

class AppOpenAdActivity: AppCompatActivity(), LifecycleEventObserver {
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String

    private var appOpenAd: PWAppOpenAd? = null
    private lateinit var statusTextView: TextView
    private lateinit var showAppOpenAdButton: Button

    override fun onDestroy() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)

        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_open_ad)
        statusTextView = findViewById(R.id.status_text_view)
        showAppOpenAdButton = findViewById(R.id.show_app_open_ad_button)
        showAppOpenAdButton.isEnabled = false
        showAppOpenAdButton.setOnClickListener {
            showAppOpenAd()
        }

        // Subscribe to listen to an app state.
        // Make sure that required dependencies are installed.
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        loadAppOpenAd()
    }

    // Observe an app state to show the ad when a user open the app.
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> { showAppOpenAd() }
            Lifecycle.Event.ON_PAUSE -> {
                // Check if we need to load app open ad before next presentation.
                if (appOpenAd != null && appOpenAd?.isLoaded == true) return
                loadAppOpenAd()
            }
            else -> {}
        }
    }

    private fun loadAppOpenAd() {
        val listener = object: PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                statusTextView.text = "✅ The app open ad \"$adUnitName\" is loaded."
                showAppOpenAdButton.isEnabled = true
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {
                statusTextView.text = "❌ Failed to load the app open ad \"$adUnitName\"."
                appOpenAd = null
            }

            override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {
                appOpenAd = null

                // Load app open ad content to be ready for the next presentation.
                loadAppOpenAd()
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {
                appOpenAd = null
                statusTextView.text = "❌ Failed to show the app open ad \"$adUnitName\"."
            }

            override fun onFullScreenAdImpression(ad: PWFullScreenAd) {
                statusTextView.text = "👍 The app open ad \"$adUnitName\" was successfully shown."
                showAppOpenAdButton.isEnabled = false
            }
        }
        appOpenAd = PWAppOpenAd(application, adUnitName, listener)

        // Ads rendered more than four hours after request time will no longer be valid and may not earn revenue.
        // Enable the property below to start loading new ad automatically if more than a certain number of hours have passed since your ad loaded.
        // It equals to `false` by default.
        appOpenAd?.autoReloadOnExpiration = true

        // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        // Use `PWLoadParams().withDeviceOrientation()` to pass the orientation you want to use in the ad request.
        // val params = PWLoadParams()
        // .withDeviceOrientation(Configuration.ORIENTATION_PORTRAIT)
        // .withTargeting(
        //   mapOf(
        //      "age" to "18-32",
        //      "page" to "travel"
        //   )
        // )
        // appOpenAd?.load(params)

        appOpenAd?.load()

        statusTextView.text = "⏳ The app open ad \"$adUnitName\" is loading."
    }

    private fun showAppOpenAd() {
        val appOpenAd = appOpenAd ?: return
        if (appOpenAd.isLoaded){
            appOpenAd.show(this)
        } else {
            // Load app open ad one more time or notify a user about error.
        }
    }
}