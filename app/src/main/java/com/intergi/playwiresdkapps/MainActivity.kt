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
    var relativeLayoput :  RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        relativeLayoput = findViewById(R.id.container)

        PlaywireSDK.loadFromAssetFile(this, "PWStoreConfig.json")
        PWAdBidder_Amazon.register(this)

        val adUnitName = "300x250 - Amazon"
        adSlot = PWAdSlot(adUnitName)

        if (BuildConfig.DEBUG) {
            val debugBuilder = PWUMPDebug.PWUMPDebugBuilder(this)
                .resettingInfo()
                .forcingEEALocation()
                .addTestDeviceHashedId("26F4F73131B7FBDD640FC59E5A4DA646")

            PlaywireSDK.umpManager.debug = debugBuilder.build()
        }

        PlaywireSDK.umpManager.requestConsent(this, {
            adSlot!!.load {
                var ad_view = PublisherAdView(this)

                ad_view.setBackgroundColor(Color.RED)

                val rLParams: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                rLParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1)
                relativeLayoput!!.addView(ad_view, rLParams);

                PWAdBannerViewHelper.loadView(adSlot!!, ad_view)
            }
        })


    }
}