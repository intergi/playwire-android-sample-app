package com.playwire.demo_kotlin.ads.view.nativead

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.intergi.playwiresdk.*
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.nativead.PWNativeView
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewFactory
import com.playwire.demo_kotlin.R
import com.playwire.demo_kotlin.misc.Constant

class NativeAdActivity: AppCompatActivity() {
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String

    private lateinit var statusTextView: TextView
    private lateinit var refreshButton: Button
    private var nativeAd: PWNativeView? = null
    private var isNativeAdded = false

    override fun onDestroy() {
        // Must call the `destroy` method to avoid memory leak.
        nativeAd?.destroy()

        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad)
        statusTextView = findViewById(R.id.status_text_view)
        refreshButton = findViewById(R.id.refresh_button)
        refreshButton.setOnClickListener {
            refresh()
        }

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        loadNativeAd()
    }

    private fun loadNativeAd() {
        val listener = object: PWViewAd.Listener {
            override fun onViewAdLoaded(ad: PWViewAd) {
                addNativeAd()
                statusTextView.text = "✅ The native ad \"$adUnitName\" is loaded."
            }
            override fun onViewAdFailedToLoad(ad: PWViewAd) {
                statusTextView.text = "❌ Failed to load the native ad \"$adUnitName\".."
            }
            override fun onViewAdOpened(ad: PWViewAd) {}
            override fun onViewAdClosed(ad: PWViewAd) {
                statusTextView.text = "👍 The native ad content \"$adUnitName\" was successfully shown."
            }
            override fun onViewAdImpression(ad: PWViewAd) {}
            override fun onViewAdClicked(ad: PWViewAd) {}
        }

        val factory = object : PWNativeViewFactory {
            override fun createAdContentView(
                nativeView: PWNativeView,
                adContent: PWNativeViewContent
            ): View {
                // Inflates your custom view which can be configurable with `PWNativeViewContent`.
                // `NativeView` is a `ViewGroup` subclass for our custom native ad layout. See `NativeView` class for more details.
                val adView = layoutInflater.inflate(R.layout.view_native_ad, null) as NativeView
                adView.configure(adContent)
                return adView
            }

            override fun callToActionView(nativeView: PWNativeView, adContentView: View): View? {
                // Defines action view to handle a user's taps on a native ad view.
                val view = adContentView as? NativeView
                return view?.actionButton
            }
        }
        nativeAd = PWNativeView(applicationContext, adUnitName, factory, listener)
        nativeAd?.load()

        // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        // val params = PWLoadParams().withTargeting(
        //   mapOf(
        //      "age" to "18-32",
        //      "page" to "travel"
        //   )
        // )
        // nativeAd?.load(params)

        statusTextView.text = "⏳ The native ad \"$adUnitName\" is loading."
    }

    private fun addNativeAd() {
        if (isNativeAdded || nativeAd == null) return
        isNativeAdded = true

        // Native view is ready to be added to view hierarchy.
        val layoutParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1)
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1)
        val container = findViewById<RelativeLayout>(R.id.native_ad_container)
        container.addView(nativeAd, layoutParams)
    }

    private fun refresh() {
        // Refresh will start only if the ad unit contains `refresh` object.
        // See logs from `PWNotifier` to track status of refresh.
        val nativeAd = nativeAd ?: return
        nativeAd.refresh()

        val adUnit = PlaywireSDK.getConfig()?.adUnits?.firstOrNull { it.name == adUnitName }
        val refresh = adUnit?.refresh
        if (refresh == null) {
            statusTextView.text = "⚠️ The native ad \"$adUnitName\" can't be refreshed manually.\nSee logs to get more details."
            return
        }
        statusTextView.text = "🔄 The native ad \"$adUnitName\" is refreshing."
    }
}