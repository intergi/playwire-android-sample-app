package com.example.demo_kotlin.ads.fullscreen.interstitial

import com.example.demo_kotlin.R
import com.example.demo_kotlin.ads.fullscreen.FullScreenAdActivity
import com.intergi.playwiresdk.ads.PWAdError
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.interstitial.PWInterstitial

class InterstitialActivity : FullScreenAdActivity() {

    private var interstitial: PWInterstitial? = null

    override fun loadAd() {
        val listener = object : PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                statusTextView.text = getString(R.string.interstitial_ad_loaded, adUnitName)
                showAd()
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd, error: PWAdError) {
                statusTextView.text = getString(R.string.interstitial_ad_load_failed, adUnitName)
            }

            override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = getString(R.string.interstitial_ad_shown, adUnitName)
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd, error: PWAdError) {
                statusTextView.text = getString(R.string.interstitial_ad_show_failed, adUnitName)
            }
        }
        interstitial = PWInterstitial(this, adUnitName, listener)
        interstitial?.load()

        statusTextView.text = getString(R.string.interstitial_ad_loading, adUnitName)
    }

    override fun showAd() {
        val interstitial = interstitial ?: return
        if (interstitial.isLoaded) {
            interstitial.show()
        }
    }
}