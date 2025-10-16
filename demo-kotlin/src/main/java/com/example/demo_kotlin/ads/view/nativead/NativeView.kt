package com.example.demo_kotlin.ads.view.nativead

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.demo_kotlin.R
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent

class NativeView: LinearLayout {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private lateinit var headlineTextView: TextView
    private lateinit var bodyTextView: TextView
    internal lateinit var actionButton: Button
    private lateinit var appIconImageView: ImageView
    private lateinit var storeTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var advertiserTextView: TextView
    private lateinit var mediaViewHolder: FrameLayout

    override fun onFinishInflate() {
        super.onFinishInflate()

        appIconImageView = findViewById(R.id.ad_app_icon)
        headlineTextView = findViewById(R.id.ad_headline)
        bodyTextView = findViewById(R.id.ad_body)
        actionButton = findViewById(R.id.ad_call_to_action)
        storeTextView = findViewById(R.id.ad_store)
        priceTextView = findViewById(R.id.ad_price)
        advertiserTextView = findViewById(R.id.ad_advertiser)
        mediaViewHolder = findViewById(R.id.ad_media)
    }

    internal fun configure(adContent: PWNativeViewContent) {
        headlineTextView.visibility = visibleIf { adContent.headline != null }
        headlineTextView.text = adContent.headline

        bodyTextView.visibility = visibleIf { adContent.body != null }
        bodyTextView.text = adContent.body

        actionButton.visibility = visibleIf { adContent.callToAction != null }
        actionButton.text = adContent.callToAction

        priceTextView.visibility = visibleIf { adContent.price != null }
        priceTextView.text = adContent.price

        storeTextView.visibility = visibleIf { adContent.store != null }
        storeTextView.text = adContent.store

        advertiserTextView.visibility = visibleIf { adContent.advertiser != null }
        advertiserTextView.text = adContent.advertiser

        appIconImageView.visibility = visibleIf { adContent.icon != null }
        appIconImageView.setImageDrawable(adContent.icon)

        mediaViewHolder.visibility = visibleIf { adContent.mediaView != null }
        if (adContent.mediaView != null) {
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.CENTER_HORIZONTAL
            params.weight = 1.0f
            adContent.mediaView?.layoutParams = params
            mediaViewHolder.addView(adContent.mediaView)
        }
    }

    private fun visibleIf(isVisible: () -> Boolean): Int = if(isVisible()) VISIBLE else INVISIBLE
}