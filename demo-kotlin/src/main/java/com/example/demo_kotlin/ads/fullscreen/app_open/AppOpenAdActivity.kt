package com.example.demo_kotlin.ads.fullscreen.app_open

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.demo_kotlin.R
import com.example.demo_kotlin.ads.fullscreen.FullScreenAdActivity
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.appopen.PWAppOpenAd

class AppOpenAdActivity : FullScreenAdActivity(), LifecycleEventObserver {
    private var appOpenAd: PWAppOpenAd? = null

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                loadAd()
            }

            Lifecycle.Event.ON_PAUSE -> {
                // Check if we need to load app open ad before next presentation.
                if (appOpenAd != null && appOpenAd?.isLoaded == true) return
                loadAd()
            }

            else -> {}
        }
    }

    override fun loadAd() {
        val listener = object : PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                statusTextView.text = getString(R.string.app_open_ad_loaded, adUnitName)
                showAd()
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {
                statusTextView.text = getString(R.string.app_open_ad_load_failed, adUnitName)
            }

            override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = getString(R.string.app_open_ad_shown, adUnitName)
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = getString(R.string.app_open_ad_show_failed, adUnitName)
            }
        }

        appOpenAd = PWAppOpenAd(application, adUnitName, listener)
        appOpenAd?.load()

        statusTextView.text = getString(R.string.app_open_ad_loading, adUnitName)
    }

    override fun showAd() {
        val appOpenAd = appOpenAd ?: return
        if (appOpenAd.isLoaded) {
            appOpenAd.show(this)
        }
    }

    override fun onStart() {
        super.onStart()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStop() {
        super.onStop()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }
}