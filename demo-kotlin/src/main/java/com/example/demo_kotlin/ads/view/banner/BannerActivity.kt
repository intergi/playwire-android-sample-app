package com.example.demo_kotlin.ads.view.banner

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.demo_kotlin.R
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.banner.PWBannerView
import com.example.demo_kotlin.misc.Constant

class BannerActivity : AppCompatActivity() {
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var statusTextView: TextView
    private lateinit var banner: PWBannerView
    private var isBannerAdded = false

    override fun onDestroy() {
        // Must call the `destroy` method to avoid memory leak.
        banner.destroy()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        constraintLayout = findViewById(R.id.container)
        statusTextView = findViewById(R.id.status_text_view)

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        title = adUnitName

        loadBanner()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadBanner() {
        val listener = object : PWViewAd.Listener {
            override fun onViewAdLoaded(ad: PWViewAd) {
                addBannerToParent()
                banner.visibility = View.VISIBLE
                statusTextView.text = getString(R.string.banner_ad_loaded, adUnitName)
            }

            override fun onViewAdFailedToLoad(ad: PWViewAd) {
                banner.visibility = View.GONE
                statusTextView.text = getString(R.string.banner_ad_load_failed, adUnitName)
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
        // banner.load(params)

        banner.visibility = View.GONE
        banner.load()

        statusTextView.text = getString(R.string.banner_ad_loading, adUnitName)
    }

    private fun addBannerToParent() {
        if (isBannerAdded) return
        isBannerAdded = true

        banner.id = View.generateViewId()
        constraintLayout.addView(banner)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintSet.connect(banner.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        constraintSet.connect(banner.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(banner.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.constrainWidth(banner.id, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainHeight(banner.id, ConstraintSet.WRAP_CONTENT)

        constraintSet.applyTo(constraintLayout)
    }
}