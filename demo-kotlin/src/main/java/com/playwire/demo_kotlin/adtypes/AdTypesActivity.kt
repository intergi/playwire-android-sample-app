package com.playwire.demo_kotlin.adtypes

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.intergi.playwiresdk.*
import com.playwire.demo_kotlin.R
import com.playwire.demo_kotlin.ads.fullscreen.appopenad.AppOpenAdActivity
import com.playwire.demo_kotlin.ads.fullscreen.interstitial.InterstitialActivity
import com.playwire.demo_kotlin.ads.fullscreen.rewarded.RewardedActivity
import com.playwire.demo_kotlin.ads.fullscreen.rewardedinterstitial.RewardedInterstitialActivity
import com.playwire.demo_kotlin.ads.view.banner.AnchoredBannerActivity
import com.playwire.demo_kotlin.ads.view.banner.BannerActivity
import com.playwire.demo_kotlin.ads.view.banner.BannerLayoutActivity
import com.playwire.demo_kotlin.ads.view.banner.InlineBannerActivity
import com.playwire.demo_kotlin.ads.view.nativead.NativeAdActivity
import com.playwire.demo_kotlin.misc.Constant
import kotlin.system.exitProcess

class AdTypesActivity: AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private var interstitialListener: PWListenerToken? = null

    override fun onDestroy() {
        // Cancel subscription once it's not needed.
        interstitialListener?.cancel()
        interstitialListener = null

        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad_types)
        statusTextView = findViewById(R.id.status_text_view)
        statusTextView.text = "⏳ SDK initializaton..."

        // Start `PWNotifier` to log SDK events to console.
        PWNotifier.startConsoleLogger()

        // Use method below to filter SDK events by name or severity.
        //
        // Filter and log only events with `PWC.EVT_gamRequestFail` name.
        // PWNotifier.startConsoleLoggerWithFilter { event, critical, context in
        //    event == PWC.EVT_gamRequestFail
        // }
        //
        // Filter and log only critical events.
        // PWNotifier.startConsoleLoggerWithFilter { _, critical, _ in
        //    critical
        // }

        // Use a custom-made listener to handle events with custom actions.
        // You can cancel subscription once it's not needed. See the `onDestroy` method.
        //
        // In the example below we create a subscription to listen to all successful interstitial loading events.

        interstitialListener = PWNotifier.addListener(
            this,
            filter = { event, critical, context ->
                event == PWC.EVT_gamRequestSuccess && context[PWC.EVT_CTX_adUnit_mode] as String == PWAdMode.Interstitial.name
            }, action = { listener, event, critical, context, data ->
                // Use event data regarding your business objectives, e.g, send analytics record, etc.
            }
        )

        // Enable test mode for debug builds to avoid `no fill` issues and be able to test your implementation with test ads.
        PlaywireSDK.test = true

        // Initialize Playwire SDK with `publisherId` and `appId`, when initialization done, you will be able to load ad units.
        // Make sure you run SDK initialization only once.
        PlaywireSDK.initialize("playwire", "test", this) {
            statusTextView.text = null
            setupListView()
        }
    }

    private fun setupListView() {
        val listView: ListView = findViewById(R.id.ad_units_list_view)

        val comparator = compareBy<Pair<PWAdMode, String>> { it.second }
        val adUnits = PlaywireSDK.adUnitNames().sortedWith(comparator).toTypedArray()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            adUnits.map { it.second }
        )
        listView.adapter = adapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, index, _ ->
                val (mode, name) = adUnits[index]
                showAdUnitActivity(name, mode)
            }
    }

    private fun showAdUnitActivity(adUnitName: String, mode: PWAdMode) {
        val activityClass = when (mode) {
            PWAdMode.Banner -> {
                // Show predefined layout activity with banner that created in the layout file
                if (adUnitName == "Banner-300x250") BannerLayoutActivity::class.java else BannerActivity::class.java
            }
            PWAdMode.BannerInline -> InlineBannerActivity::class.java
            PWAdMode.BannerAnchored -> AnchoredBannerActivity::class.java
            PWAdMode.Interstitial -> InterstitialActivity::class.java
            PWAdMode.Rewarded -> RewardedActivity::class.java
            PWAdMode.AppOpenAd -> AppOpenAdActivity::class.java
            PWAdMode.RewardedInterstitial -> RewardedInterstitialActivity::class.java
            PWAdMode.Native -> NativeAdActivity::class.java
            else -> { exitProcess(0) }
        }

        val intent = Intent(this, activityClass)
        intent.putExtra(Constant.adUnitNameKey, adUnitName)
        startActivity(intent)
    }
}