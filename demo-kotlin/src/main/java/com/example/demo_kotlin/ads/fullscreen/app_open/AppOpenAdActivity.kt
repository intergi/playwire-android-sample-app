package com.example.demo_kotlin.ads.fullscreen.app_open

import com.example.demo_kotlin.R
import com.example.demo_kotlin.ads.fullscreen.FullScreenAdActivity
import com.intergi.playwiresdk.ads.PWAdError
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.appopen.PWAppOpenAd

class AppOpenAdActivity : FullScreenAdActivity() {
    private var appOpenAd: PWAppOpenAd? = null

    override fun loadAd() {
        val listener = object : PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                statusTextView.text = getString(R.string.app_open_ad_loaded, adUnitName)
                showAd()
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd, error: PWAdError) {
                statusTextView.text = getString(R.string.app_open_ad_load_failed, adUnitName)
            }

            override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = getString(R.string.app_open_ad_shown, adUnitName)
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd, error: PWAdError) {
                statusTextView.text = getString(R.string.app_open_ad_show_failed, adUnitName)
            }
        }

        appOpenAd = PWAppOpenAd(application, adUnitName, listener)
        appOpenAd?.autoReloadOnExpiration = true
        appOpenAd?.load()

        statusTextView.text = getString(R.string.app_open_ad_loading, adUnitName)
    }

    override fun showAd() {
        val appOpenAd = appOpenAd ?: return
        if (appOpenAd.isLoaded) {
            appOpenAd.show(this)
        }
    }
}