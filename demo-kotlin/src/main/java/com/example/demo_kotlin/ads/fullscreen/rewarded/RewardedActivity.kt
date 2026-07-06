package com.example.demo_kotlin.ads.fullscreen.rewarded

import com.example.demo_kotlin.R
import com.example.demo_kotlin.ads.fullscreen.FullScreenAdActivity
import com.intergi.playwiresdk.ads.PWAdError
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.rewarded.PWRewarded

class RewardedActivity : FullScreenAdActivity() {

    private var rewarded: PWRewarded? = null

    override fun loadAd() {
        val listener = object : PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                statusTextView.text = getString(R.string.rewarded_ad_loaded, adUnitName)
                showAd()
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd, error: PWAdError) {
                statusTextView.text = getString(R.string.rewarded_ad_load_failed, adUnitName)
            }

            override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = getString(R.string.rewarded_ad_shown, adUnitName)
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd, error: PWAdError) {
                statusTextView.text = getString(R.string.rewarded_ad_show_failed, adUnitName)
            }

            override fun onFullScreenAdReward(ad: PWFullScreenAd, type: String, amount: Int) {
                statusTextView.text = getString(R.string.rewarded_ad_earned, type, amount)
            }
        }

        rewarded = PWRewarded(this, adUnitName, listener)
        rewarded?.load()

        statusTextView.text = getString(R.string.rewarded_ad_loading, adUnitName)
    }

    override fun showAd() {
        val rewarded = rewarded ?: return
        if (rewarded.isLoaded) {
            rewarded.show()
        }
    }
}