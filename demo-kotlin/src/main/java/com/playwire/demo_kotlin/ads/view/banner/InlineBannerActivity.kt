package com.playwire.demo_kotlin.ads.view.banner

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.intergi.playwiresdk.PlaywireSDK
import com.intergi.playwiresdk.ads.PWLoadParams
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.banner.PWBannerViewInline
import com.playwire.demo_kotlin.R
import com.playwire.demo_kotlin.misc.Constant

class InlineBannerActivity: AppCompatActivity() {
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String

    private lateinit var bannerContainer: RelativeLayout
    private lateinit var statusTextView: TextView
    private lateinit var refreshButton: Button
    private var banner: PWBannerViewInline? = null
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

        banner = PWBannerViewInline(this, adUnitName, listener)

        // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        // Use `PWLoadParams().withWidth()` to pass available width to fill with ad content.
        // Get the width of the device in use, or set your own width if you don’t want to use the full width of the screen.
        // Use `PWLoadParams().withDeviceOrientation()` to include the orientation of your interface to ad request.
        // Use `ORIENTATION_UNDEFINED` value to allow SDK to determine the orientation.
        // val params = PWLoadParams().withTargeting(
        //   mapOf(
        //      "age" to "18-32",
        //      "page" to "travel"
        //   )
        // )
        // .withWidth(320)
        // .withDeviceOrientation(Configuration.ORIENTATION_UNDEFINED)
        // banner?.load(params)

        val params = PWLoadParams()
            .withWidth(320)
            .withDeviceOrientation(Configuration.ORIENTATION_UNDEFINED)
        banner?.load(params)

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