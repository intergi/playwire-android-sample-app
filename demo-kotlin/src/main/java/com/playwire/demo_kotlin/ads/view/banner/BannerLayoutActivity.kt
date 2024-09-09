package com.playwire.demo_kotlin.ads.view.banner

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.banner.PWBannerView
import com.playwire.demo_kotlin.R

class BannerLayoutActivity: AppCompatActivity() {
    private lateinit var statusTextView: TextView
    private lateinit var banner: PWBannerView
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    // See layout file to set ad unit name there.
    private val adUnitName: String = "Banner-300x250"

    override fun onDestroy() {
        // Must call the `destroy` method to avoid memory leak.
        banner?.destroy()

        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner_layout)
        statusTextView = findViewById(R.id.status_text_view)

        val listener = object: PWViewAd.Listener {
            override fun onViewAdLoaded(ad: PWViewAd) {
                statusTextView.text = "✅ The banner \"$adUnitName\" is loaded."
            }

            override fun onViewAdFailedToLoad(ad: PWViewAd) {
                statusTextView.text = "❌ Failed to load the banner \"$adUnitName\"."
            }
        }

        banner = findViewById(R.id.static_banner_view)
        banner.listener = listener
        statusTextView.text = "⏳ The banner \"$adUnitName\" is loading."
    }
}