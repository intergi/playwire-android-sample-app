package com.playwire.demo_kotlin.ads.fullscreen.rewardedinterstitial

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.rewardedinterstitial.PWRewardedInterstitial
import com.playwire.demo_kotlin.R
import com.playwire.demo_kotlin.misc.Constant

class RewardedInterstitialActivity: AppCompatActivity() {
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String

    private var rewardedInterstitial: PWRewardedInterstitial? = null

    private lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewarded_interstitial)
        statusTextView = findViewById(R.id.status_text_view)

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        loadRewarded()
    }

    private fun loadRewarded() {
        val listener = object : PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                statusTextView.text = "✅ The rewarded interstitial \"$adUnitName\" is loaded."
                showRewardedInterstitial()
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {
                statusTextView.text = "❌ Failed to load the rewarded interstitial \"$adUnitName\"."
            }

            override fun onFullScreenAdShowedFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = "👍 The rewarded interstitial \"$adUnitName\" was successfully shown."
            }

            override fun onFullScreenAdReward(ad: PWFullScreenAd, type: String, amount: Int) {
                // Handle a reward regarding your business objectives.
                statusTextView.text = "🎉 The reward is earned.\n Type: $type \n Amount: $amount"
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = "❌ Failed to show the rewarded interstitial \"$adUnitName\"."
            }

        }
        
        rewardedInterstitial = PWRewardedInterstitial(this, adUnitName, listener)
        rewardedInterstitial?.load()

        // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        // val params = PWLoadParams().withTargeting(
        //   mapOf(
        //      "age" to "18-32",
        //      "page" to "travel"
        //   )
        // )
        // rewardedInterstitial?.load(params)

        statusTextView.text = "⏳ The rewarded interstitial \"$adUnitName\" is loading."
    }

    private fun showRewardedInterstitial() {
        val rewardedInterstitial = rewardedInterstitial ?: return
        if (rewardedInterstitial.isLoaded) {
            rewardedInterstitial.show()
        } else {
            // Load rewarded interstitial one more time or notify a user about error.
        }
    }

}