package com.example.demo_kotlin.ad_types

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import com.intergi.playwiresdk.PlaywireSDK
import com.intergi.playwiresdk.logger.LogLevel

class AdTypesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdTypesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_types)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // log SDK events to console.
        PlaywireSDK.setLogLevel(LogLevel.INFO)
        // Set to `true` to test your implementation with GAM test ads on real devices.
        PlaywireSDK.test = false

        // Initialize Playwire SDK with `publisherId` and `appId`, when initialization done, you will be able to load ad units.
        // Make sure you run SDK initialization only once.
        PlaywireSDK.start("1024407", "703", this) { success, error ->
            if (success) {
                setupRecyclerView()
            } else {
                Toast.makeText(
                    this,
                    error?.message ?: "Playwire SDK initialization failed.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.ad_units_recycler_view)

        adapter = AdTypesAdapter { (adUnitName, activityClass) ->
            val intent = Intent(this, activityClass)
            intent.putExtra(Constant.adUnitNameKey, adUnitName)
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adUnits: List<Pair<String, Class<out Activity>>> = listOf(
            Pair("banner-320x50-gam", BannerActivity::class.java),
            Pair("banner-320x50-max", BannerActivity::class.java),
            Pair("banner-300x250-gam", BannerActivity::class.java),
            Pair("banner-300x250-max", BannerActivity::class.java),
            Pair("native-gam", NativeAdActivity::class.java),
            Pair("native-max", NativeAdActivity::class.java),
            Pair("app-open-gam", AppOpenAdActivity::class.java),
            Pair("app-open-max", AppOpenAdActivity::class.java),
            Pair("interstitial-gam", InterstitialActivity::class.java),
            Pair("interstitial-max", InterstitialActivity::class.java),
            Pair("rewarded-gam", RewardedActivity::class.java),
            Pair("rewarded-video-max", RewardedActivity::class.java),
            Pair("floating-banner", BannerActivity::class.java)
        )
        adapter.submitList(adUnits)
    }
}