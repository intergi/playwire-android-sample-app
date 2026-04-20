package com.example.demo_kotlin.ads.view.nativead

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.demo_kotlin.R
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContentView
import com.intergi.playwiresdk.extensions.dp

class NativeView : ConstraintLayout, PWNativeViewContentView {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    override val headlineTextView: TextView
        get() = findViewById(R.id.ad_headline)

    override val adAttributionView: TextView
        get() = findViewById(R.id.ad_attribution)

    override val mediaView: ViewGroup
        get() = findViewById(R.id.ad_media)

    override val callToActionButton: Button?
        get() = findViewById(R.id.ad_call_to_action)

    override val bodyTextView: TextView?
        get() = findViewById(R.id.ad_body)

    override val iconImageView: ImageView?
        get() = findViewById(R.id.ad_app_icon)

    override val advertiserTextView: TextView?
        get() = findViewById(R.id.ad_advertiser)

    override val storeTextView: TextView?
        get() = findViewById(R.id.ad_store)

    override val priceTextView: TextView?
        get() = findViewById(R.id.ad_price)

    override fun didSetAdContent(adContent: PWNativeViewContent) {
        val params = mediaView.layoutParams as? LayoutParams ?: return

        params.dimensionRatio = null

        if (adContent.mediaAspectRatio == null) {
            params.height = 120.dp
        } else {
            params.height = LayoutParams.WRAP_CONTENT
        }

        mediaView.layoutParams = params
    }
}