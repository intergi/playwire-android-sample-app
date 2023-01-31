package com.playwire.demo_kotlin.ads.view.banner

import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.intergi.playwiresdk.PlaywireSDK
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.banner.PWBannerView
import com.playwire.demo_kotlin.R
import com.playwire.demo_kotlin.misc.Constant

class BannerActivity: AppCompatActivity() {
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String

    private lateinit var bannerContainer: RelativeLayout
    private lateinit var statusTextView: TextView
    private lateinit var refreshButton: Button
    private var banner: PWBannerView? = null
    private var isBannerAdded = false

    override fun onDestroy() {
        super.onDestroy()

        // Must call the `destroy` method to avoid memory leak.
        banner?.destroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)
        bannerContainer = findViewById(R.id.banner_container)
        statusTextView = findViewById(R.id.status_text_view)
        refreshButton = findViewById(R.id.refresh_button)
        refreshButton.setOnClickListener {
            refresh()
        }

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        loadBanner()
    }

    private fun loadBanner() {
        val listener = object: PWViewAd.Listener {
            override fun onViewAdLoaded(ad: PWViewAd) {
                statusTextView.text = "✅ The banner \"$adUnitName\" is loaded."
                addBannerToParent()
            }

            override fun onViewAdFailedToLoad(ad: PWViewAd) {
                statusTextView.text = "❌ Failed to load the banner \"$adUnitName\"."
            }
        }

        banner = PWBannerView(this, adUnitName, listener)

        // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        // val params = PWLoadParams().withTargeting(
        //   mapOf(
        //      "age" to "18-32",
        //      "page" to "travel"
        //   )
        // )
        // banner?.load(params)

        banner?.load()

        statusTextView.text = "⏳ The banner \"$adUnitName\" is loading."
    }

    private fun addBannerToParent() {
        if (isBannerAdded || banner == null) return

        isBannerAdded = true

        // Banner is ready to be added to view hierarchy.
        val layoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1)
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1)
        bannerContainer.addView(banner, layoutParams)
    }

    private fun refresh() {
        // Refresh will start only if the ad unit contains `refresh` object.
        // See logs from `PWNotifier` to track status of refresh.

        val banner = banner ?: return
        banner.refresh()

        val adUnit = PlaywireSDK.getConfig()?.adUnits?.firstOrNull { it.name == adUnitName }
        val refresh = adUnit?.refresh
        if (refresh == null) {
            statusTextView.text = "⚠️ The banner \"$adUnitName\" can't be refreshed manually.\nSee logs to get more details."
            return
        }
        statusTextView.text = "🔄 The banner \"$adUnitName\" is refreshing."
    }
}