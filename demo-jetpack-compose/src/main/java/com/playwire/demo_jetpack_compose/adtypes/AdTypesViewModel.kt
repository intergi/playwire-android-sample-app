package com.playwire.demo_jetpack_compose.adtypes

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.intergi.playwiresdk.PWAdMode
import com.intergi.playwiresdk.PWNotifier
import com.intergi.playwiresdk.PlaywireSDK
import com.playwire.demo_jetpack_compose.ads.fullscreen.appopenad.AppOpenAdActivity
import com.playwire.demo_jetpack_compose.ads.fullscreen.interstitial.InterstitialActivity
import com.playwire.demo_jetpack_compose.ads.fullscreen.rewarded.RewardedActivity
import com.playwire.demo_jetpack_compose.ads.fullscreen.rewardedinterstitial.RewardedInterstitialActivity
import com.playwire.demo_jetpack_compose.ads.view.banner.AnchoredBannerActivity
import com.playwire.demo_jetpack_compose.ads.view.banner.BannerActivity
import com.playwire.demo_jetpack_compose.ads.view.banner.InlineBannerActivity
import com.playwire.demo_jetpack_compose.misc.Constant
import kotlin.system.exitProcess

class AdTypesViewModel(val activity: Activity): ViewModel() {

    var isSDKInitialized by mutableStateOf(false)
        private set
    var adUnits by mutableStateOf(listOf<Pair<PWAdMode, String>>())
        private set
    fun initialize() {

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

        // Enable test mode for debug builds to avoid `no fill` issues and be able to test your implementation with test ads.
        // PlaywireSDK.test = true

        // Initialize Playwire SDK with `publisherId` and `appId`, when initialization done, you will be able to load ad units.
        // Make sure you run SDK initialization only once.
        PlaywireSDK.initialize("playwire", "test", activity) {
            isSDKInitialized = true

            val comparator = compareBy<Pair<PWAdMode, String>> { it.second }
            adUnits = PlaywireSDK.adUnitNames().sortedWith(comparator)
        }
    }

    fun showAdUnitActivity(adUnitName: String, mode: PWAdMode) {
        val activityClass = when (mode) {
            PWAdMode.Banner -> BannerActivity::class.java
            PWAdMode.BannerInline -> InlineBannerActivity::class.java
            PWAdMode.BannerAnchored -> AnchoredBannerActivity::class.java
            PWAdMode.Interstitial -> InterstitialActivity::class.java
            PWAdMode.Rewarded -> RewardedActivity::class.java
            PWAdMode.AppOpenAd -> AppOpenAdActivity::class.java
            PWAdMode.RewardedInterstitial -> RewardedInterstitialActivity::class.java
            else -> { return }
        }

        val intent = Intent(activity, activityClass)
        intent.putExtra(Constant.adUnitNameKey, adUnitName)
        activity.startActivity(intent)
    }
}