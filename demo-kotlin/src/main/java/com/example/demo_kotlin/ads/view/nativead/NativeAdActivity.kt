package com.example.demo_kotlin.ads.view.nativead

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.demo_kotlin.R
import com.example.demo_kotlin.misc.Constant
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.nativead.PWNativeView
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContentView
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewFactory

class NativeAdActivity : AppCompatActivity() {

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

        loadNativeAd()
    }

    override fun onDestroy() {
        nativeAd.destroy()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadNativeAd() {
        val listener = object : PWViewAd.Listener {
            override fun onViewAdLoaded(ad: PWViewAd) {
                addNativeAd()
                statusTextView.text = getString(R.string.native_ad_loaded, adUnitName)
            }

            override fun onViewAdFailedToLoad(ad: PWViewAd) {
                statusTextView.text = getString(R.string.native_ad_load_failed, adUnitName)
            }

            override fun onViewAdClosed(ad: PWViewAd) {
                statusTextView.text = getString(R.string.native_ad_shown, adUnitName)
            }

            override fun onViewAdOpened(ad: PWViewAd) {
                statusTextView.text = getString(R.string.native_ad_opened, adUnitName)
            }

            override fun onViewAdImpression(ad: PWViewAd) {
                statusTextView.text = getString(R.string.native_ad_impressed, adUnitName)
            }

            override fun onViewAdClicked(ad: PWViewAd) {
                statusTextView.text = getString(R.string.native_ad_clicked, adUnitName)
            }
        }

        val factory = object : PWNativeViewFactory {
            override fun createAdContentView(): PWNativeViewContentView {
                return layoutInflater.inflate(
                    R.layout.view_native_ad,
                    null as ViewGroup?
                ) as NativeView
            }
        }

        nativeAd = PWNativeView(this, adUnitName, factory, listener)
        nativeAd.load()

        statusTextView.text = getString(R.string.native_ad_loading, adUnitName)
    }

    private fun addNativeAd() {
        if (isNativeAdded) return
        isNativeAdded = true

        nativeAd.id = View.generateViewId()

        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        }

        constraintLayout.addView(nativeAd, layoutParams)

        val statusParams = statusTextView.layoutParams as ConstraintLayout.LayoutParams
        statusParams.bottomToTop = nativeAd.id
        statusTextView.layoutParams = statusParams
    }
}