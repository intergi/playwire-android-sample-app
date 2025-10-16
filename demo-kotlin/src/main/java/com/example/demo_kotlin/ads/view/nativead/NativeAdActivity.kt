package com.example.demo_kotlin.ads.view.nativead

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.nativead.PWNativeView
import com.example.demo_kotlin.misc.Constant
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewFactory
import com.example.demo_kotlin.R

class NativeAdActivity: AppCompatActivity() {
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var statusTextView: TextView
    private lateinit var nativeAd: PWNativeView
    private var isNativeAdded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad)

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        title = adUnitName

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        constraintLayout = findViewById(R.id.container)
        statusTextView = findViewById(R.id.status_text_view)

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        loadNativeAd()
    }

    override fun onDestroy() {
        nativeAd?.destroy()
        super.onDestroy()
    }

    private fun loadNativeAd() {
        val listener = object : PWViewAd.Listener {
            override fun onViewAdLoaded(ad: PWViewAd) {
                addNativeAd()

                statusTextView.text =
                    getString(com.example.demo_kotlin.R.string.native_ad_loaded, adUnitName)
            }

            override fun onViewAdFailedToLoad(ad: PWViewAd) {
                statusTextView.text =
                    getString(com.example.demo_kotlin.R.string.native_ad_load_failed, adUnitName)
            }

            override fun onViewAdOpened(ad: PWViewAd) {}

            override fun onViewAdClosed(ad: PWViewAd) {
                statusTextView.text =
                    getString(com.example.demo_kotlin.R.string.native_ad_shown, adUnitName)
            }

            override fun onViewAdImpression(ad: PWViewAd) {}

            override fun onViewAdClicked(ad: PWViewAd) {}
        }

        val factory = object : PWNativeViewFactory {
            override fun createAdContentView(
                nativeView: PWNativeView,
                adContent: PWNativeViewContent
            ): View {
                val adView = layoutInflater.inflate(R.layout.view_native_ad, null as ViewGroup?) as NativeView
                adView.configure(adContent)
                return adView
            }

            override fun callToActionView(nativeView: PWNativeView, adContentView: View): View? {
                val view = adContentView as? NativeView
                return view?.actionButton
            }
        }
        nativeAd = PWNativeView(this, adUnitName, factory, listener)
        nativeAd?.load()

        statusTextView.text =
            getString(R.string.native_ad_loading, adUnitName)
    }

    private fun addNativeAd() {
        if (isNativeAdded) return
        isNativeAdded = true

        // The native ad view needs an ID to be constrained
        nativeAd.id = View.generateViewId()

        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT, // Or WRAP_CONTENT, depending on desired width
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        // Add constraints to position the ad
        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID

        constraintLayout.addView(nativeAd, layoutParams)

        // Update the constraints of the statusTextView to be above the native ad
        val statusParams = statusTextView.layoutParams as ConstraintLayout.LayoutParams
        statusParams.bottomToTop = nativeAd.id // Constrain bottom of TextView to top of Ad
        statusTextView.layoutParams = statusParams
    }
}