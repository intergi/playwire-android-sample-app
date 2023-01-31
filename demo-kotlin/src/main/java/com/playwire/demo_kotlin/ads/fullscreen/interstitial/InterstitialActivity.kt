package com.playwire.demo_kotlin.ads.fullscreen.interstitial

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.interstitial.PWInterstitial
import com.playwire.demo_kotlin.R
import com.playwire.demo_kotlin.misc.Constant

class InterstitialActivity: AppCompatActivity() {
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String
    
    private var interstitial: PWInterstitial? = null
    private lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interstitial)
        statusTextView = findViewById(R.id.status_text_view)
        
        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        loadInterstitial()
    }

    private fun loadInterstitial() {
        val listener = object: PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                statusTextView.text = "✅ The interstitial \"$adUnitName\" is loaded."
                showInterstitial()
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {
                statusTextView.text = "❌ Failed to load the interstitial \"$adUnitName\"."
            }

            override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = "👍 The interstitial \"$adUnitName\" was successfully shown."
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {
                statusTextView.text = "❌ Failed to show the interstitial \"$adUnitName\"."
            }
        }
        interstitial = PWInterstitial(this, adUnitName, listener)
        interstitial?.load()

        // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        // val params = PWLoadParams().withTargeting(
        //   mapOf(
        //      "age" to "18-32",
        //      "page" to "travel"
        //   )
        // )
        // interstitial?.load(params)

        statusTextView.text = "⏳ The interstitial \"$adUnitName\" is loading."
    }

    private fun showInterstitial() {
        val interstitial = interstitial ?: return
        if (interstitial.isLoaded){
            interstitial.show()
        } else {
            // Load interstitial one more time or notify a user about error.
        }
    }
}
