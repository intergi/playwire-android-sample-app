package com.playwire.demo_kotlin.ads.fullscreen.rewarded

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.rewarded.PWRewarded
import com.playwire.demo_kotlin.R
import com.playwire.demo_kotlin.misc.Constant

class RewardedActivity: AppCompatActivity() {
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String

    private var rewarded: PWRewarded? = null

    private lateinit var statusTextView: TextView
    private lateinit var getRewardButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewarded)
        statusTextView = findViewById(R.id.status_text_view)
        getRewardButton = findViewById(R.id.get_reward_button)
        getRewardButton.setOnClickListener { showRewarded() }

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""

        loadRewarded()
    }

    private fun loadRewarded() {
        val listener = object: PWFullScreenAd.Listener {

            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                statusTextView.text = "✅ The rewarded \"$adUnitName\" is loaded."
                getRewardButton.isEnabled = true
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {
                statusTextView.text = "❌ Failed to load the rewarded \"$adUnitName\"."
            }

            override fun onFullScreenAdShowedFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = "👍 The rewarded \"$adUnitName\" was successfully shown."
                getRewardButton.isEnabled = false
            }

            override fun onFullScreenAdReward(ad: PWFullScreenAd, type: String, amount: Int) {
                // Handle a reward regarding your business objectives.
                statusTextView.text = "🎉 The reward is earned.\n Type: $type \n Amount: $amount"
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = "❌ Failed to show the rewarded \"$adUnitName\"."
            }

        }
        rewarded = PWRewarded(this, adUnitName, listener)
        rewarded?.load()

        // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        // val params = PWLoadParams().withTargeting(
        //   mapOf(
        //      "age" to "18-32",
        //      "page" to "travel"
        //   )
        // )
        // rewarded?.load(params)

        getRewardButton.isEnabled = false
        statusTextView.text = "⏳ The rewarded \"$adUnitName\" is loading."
    }

    private fun showRewarded() {
        val rewarded = rewarded ?: return
        if (rewarded.isLoaded){
            rewarded.show()
        } else {
            // Load rewarded one more time or notify a user about error.
            loadRewarded()
        }
    }
}