package com.intergi.playwiresdkapps

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.google.android.gms.ads.doubleclick.PublisherAdView
import com.intergi.playwiresdk.PWAdBannerViewHelper

import com.intergi.playwiresdk.PWAdSlot
import com.intergi.playwiresdk.PWUMPDebug
import com.intergi.playwiresdk.PlaywireSDK
import com.intergi.playwiresdk_amazon.PWAdBidder_Amazon

class MainActivity : AppCompatActivity() {

    var adSlot : PWAdSlot? = null

    // we only are able to ask for an ad only after users are given their consent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureAdManager() {showAd()}
    }

    private fun configureAdManager(onReady: ()->Unit) {
        // load configuration from json file
        PlaywireSDK.loadFromAssetFile(this, "PWStoreConfig.json")
        // use amazon header bidding
        PWAdBidder_Amazon.register(this)

        // in debug pretend user are in EU
        if (BuildConfig.DEBUG) {
            val debugBuilder = PWUMPDebug.PWUMPDebugBuilder(this)
                .resettingInfo()
                .forcingEEALocation()
                .addTestDeviceHashedId("26F4F73131B7FBDD640FC59E5A4DA646")

            PlaywireSDK.umpManager.debug = debugBuilder.build()
        }

        // request user consent with a helper into the sdk
        // result is async
        PlaywireSDK.umpManager.requestConsent(this, {
            onReady()
        })

    }

    private fun showAd(){
        // create adslot given a known name in config
        val adUnitName = "300x250 - Amazon"
        adSlot = PWAdSlot(adUnitName)

        // prebid and create view after prebid result
        adSlot!!.load {
            var ad_view = PublisherAdView(this)
            addAdView(ad_view)
            PWAdBannerViewHelper.loadView(adSlot!!, ad_view)
        }

    }

    private fun addAdView(ad_view: PublisherAdView ) {
        val relativeLayoput : RelativeLayout = findViewById(R.id.container)

        ad_view.setBackgroundColor(Color.RED)

        val rLParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1)
        relativeLayoput!!.addView(ad_view, rLParams);
    }
}