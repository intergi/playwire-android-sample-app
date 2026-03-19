package com.example.demo_kotlin.ad_types

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_kotlin.R
import com.example.demo_kotlin.ads.fullscreen.app_open.AppOpenAdActivity
import com.example.demo_kotlin.ads.fullscreen.interstitial.InterstitialActivity
import com.example.demo_kotlin.ads.fullscreen.rewarded.RewardedActivity
import com.example.demo_kotlin.ads.view.banner.BannerActivity
import com.example.demo_kotlin.ads.view.nativead.NativeAdActivity
import com.example.demo_kotlin.misc.Constant
import com.intergi.playwiresdk.PWAdMode
import com.intergi.playwiresdk.PWNotifier
import com.intergi.playwiresdk.PlaywireSDK

class AdTypesActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdTypesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_types)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Start `PWNotifier` to log SDK events to console.
        PWNotifier.startConsoleLogger()
        // Set to `true` to test your implementation with GAM test ads on real devices.
        PlaywireSDK.test = false

        // Initialize Playwire SDK with `publisherId` and `appId`, when initialization done, you will be able to load ad units.
        // Make sure you run SDK initialization only once.
        PlaywireSDK.initialize("1024407", "703", this) {
            setupRecyclerView()
        }
    }

    private fun setupRecyclerView() {
        val adUnits = PlaywireSDK.getConfig()?.adUnits?.map {
            Pair(it.mode, it.name)
        } ?: emptyList()

        recyclerView = findViewById(R.id.ad_units_recycler_view)

        adapter = AdTypesAdapter { selectedAd ->
            val adUnitName = selectedAd.second
            val adMode = selectedAd.first
            showAdUnitActivity(adUnitName, adMode)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.submitList(adUnits)
    }

    private fun showAdUnitActivity(adUnitName: String, mode: PWAdMode) {
        val activityClass = when (mode) {
            PWAdMode.Banner -> BannerActivity::class.java
            PWAdMode.Interstitial -> InterstitialActivity::class.java
            PWAdMode.Rewarded -> RewardedActivity::class.java
            PWAdMode.AppOpenAd -> AppOpenAdActivity::class.java
            PWAdMode.Native -> NativeAdActivity::class.java
            else -> { throw IllegalArgumentException("Invalid ad mode: $mode") }
        }

        val intent = Intent(this, activityClass)
        intent.putExtra(Constant.adUnitNameKey, adUnitName)
        startActivity(intent)
    }
}