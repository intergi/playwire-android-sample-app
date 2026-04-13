package com.example.demo_compose.ads.view.nativead

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.demo_compose.R
import com.example.demo_compose.misc.Constant
import com.example.demo_compose.ui.theme.PlaywireAppTheme
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.nativead.PWNativeView
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContentView
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewFactory

class NativeAdActivity : ComponentActivity() {

    private var nativeAd: PWNativeView? = null
    private var nativeAdHost: FrameLayout? = null
    private lateinit var adUnitName: String
    private var statusText by mutableStateOf("Ad status will be displayed here")
    private var isAdLoaded by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""

        setContent {
            PlaywireAppTheme {
                NativeAdScreen(
                    adUnitName = adUnitName,
                    statusText = statusText,
                    isAdLoaded = isAdLoaded,
                    nativeAdHostProvider = { nativeAdHost },
                    onNavigateUp = { onBackPressedDispatcher.onBackPressed() }
                )
            }
        }

        loadNativeAd()
    }

    private fun loadNativeAd() {
        val factory = object : PWNativeViewFactory {
            override fun createAdContentView(): PWNativeViewContentView {
                return ComposeNativeContentView(this@NativeAdActivity)
            }
        }

        val listener = object : PWViewAd.Listener {
            override fun onViewAdLoaded(ad: PWViewAd) {
                statusText = getString(R.string.native_ad_loaded, adUnitName)
                isAdLoaded = true

                nativeAd?.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                nativeAd?.requestLayout()
                nativeAdHost?.requestLayout()
            }

            override fun onViewAdFailedToLoad(ad: PWViewAd) {
                statusText = getString(R.string.native_ad_load_failed, adUnitName)
            }

            override fun onViewAdOpened(ad: PWViewAd) {
                statusText = "Native ad opened: $adUnitName"
            }

            override fun onViewAdClosed(ad: PWViewAd) {
                statusText = getString(R.string.native_ad_shown, adUnitName)
            }

            override fun onViewAdImpression(ad: PWViewAd) {
                statusText = "Native ad impression recorded: $adUnitName"
            }

            override fun onViewAdClicked(ad: PWViewAd) {
                statusText = "Native ad clicked: $adUnitName"
            }
        }

        nativeAd = PWNativeView(this, adUnitName, factory, listener).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        nativeAdHost = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            addView(nativeAd)
        }

        nativeAd?.load()
        statusText = getString(R.string.native_ad_loading, adUnitName)
    }

    override fun onDestroy() {
        nativeAd?.destroy()
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NativeAdScreen(
    adUnitName: String,
    statusText: String,
    isAdLoaded: Boolean,
    nativeAdHostProvider: () -> FrameLayout?,
    onNavigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = adUnitName) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(text = statusText)

            Spacer(modifier = Modifier.height(16.dp))

            if (isAdLoaded) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { nativeAdHostProvider()!! },
                    update = { host ->
                        host.layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        host.requestLayout()
                    }
                )
            }
        }
    }
}

private class ComposeNativeContentView(context: Context) :
    ConstraintLayout(context), PWNativeViewContentView {

    private val attributionView = TextView(context).apply {
        id = View.generateViewId()
        text = "Ad"
        textSize = 10f
        setPadding(12, 6, 12, 6)
    }

    private val iconView = ImageView(context).apply {
        id = View.generateViewId()
    }

    private val headlineView = TextView(context).apply {
        id = View.generateViewId()
        textSize = 16f
    }

    private val advertiserView = TextView(context).apply {
        id = View.generateViewId()
        textSize = 14f
    }

    private val bodyView = TextView(context).apply {
        id = View.generateViewId()
        textSize = 12f
    }

    private val mediaContainer = FrameLayout(context).apply {
        id = View.generateViewId()
        minimumHeight = 220
    }

    private val priceView = TextView(context).apply {
        id = View.generateViewId()
        textSize = 12f
    }

    private val storeView = TextView(context).apply {
        id = View.generateViewId()
        textSize = 12f
    }

    private val ctaButton = Button(context).apply {
        id = View.generateViewId()
        textSize = 12f
    }

    init {
        id = View.generateViewId()
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setPadding(32, 20, 32, 24)

        addView(attributionView)
        addView(iconView)
        addView(headlineView)
        addView(advertiserView)
        addView(bodyView)
        addView(mediaContainer)
        addView(priceView)
        addView(storeView)
        addView(ctaButton)

        val set = ConstraintSet()
        set.clone(this)

        set.connect(attributionView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(attributionView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)

        set.constrainWidth(iconView.id, 72)
        set.constrainHeight(iconView.id, 72)
        set.connect(iconView.id, ConstraintSet.TOP, attributionView.id, ConstraintSet.BOTTOM, 16)
        set.connect(iconView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)

        set.connect(headlineView.id, ConstraintSet.TOP, iconView.id, ConstraintSet.TOP)
        set.connect(headlineView.id, ConstraintSet.START, iconView.id, ConstraintSet.END, 16)
        set.connect(headlineView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        set.constrainWidth(headlineView.id, 0)

        set.connect(advertiserView.id, ConstraintSet.TOP, headlineView.id, ConstraintSet.BOTTOM, 8)
        set.connect(advertiserView.id, ConstraintSet.START, headlineView.id, ConstraintSet.START)
        set.connect(advertiserView.id, ConstraintSet.END, headlineView.id, ConstraintSet.END)
        set.constrainWidth(advertiserView.id, 0)

        set.connect(bodyView.id, ConstraintSet.TOP, iconView.id, ConstraintSet.BOTTOM, 16)
        set.connect(bodyView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        set.connect(bodyView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        set.constrainWidth(bodyView.id, 0)

        set.connect(mediaContainer.id, ConstraintSet.TOP, bodyView.id, ConstraintSet.BOTTOM, 16)
        set.connect(mediaContainer.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        set.connect(mediaContainer.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        set.constrainWidth(mediaContainer.id, 0)
        set.constrainHeight(mediaContainer.id, 220)

        set.connect(ctaButton.id, ConstraintSet.TOP, mediaContainer.id, ConstraintSet.BOTTOM, 16)
        set.connect(ctaButton.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        set.connect(ctaButton.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

        set.connect(storeView.id, ConstraintSet.END, ctaButton.id, ConstraintSet.START, 16)
        set.connect(storeView.id, ConstraintSet.TOP, ctaButton.id, ConstraintSet.TOP)
        set.connect(storeView.id, ConstraintSet.BOTTOM, ctaButton.id, ConstraintSet.BOTTOM)

        set.connect(priceView.id, ConstraintSet.END, storeView.id, ConstraintSet.START, 16)
        set.connect(priceView.id, ConstraintSet.TOP, ctaButton.id, ConstraintSet.TOP)
        set.connect(priceView.id, ConstraintSet.BOTTOM, ctaButton.id, ConstraintSet.BOTTOM)

        set.applyTo(this)
    }

    override val headlineTextView: TextView
        get() = headlineView

    override val adAttributionView: TextView
        get() = attributionView

    override val mediaView: ViewGroup
        get() = mediaContainer

    override val callToActionButton: Button?
        get() = ctaButton

    override val bodyTextView: TextView?
        get() = bodyView

    override val iconImageView: ImageView?
        get() = iconView

    override val advertiserTextView: TextView?
        get() = advertiserView

    override val storeTextView: TextView?
        get() = storeView

    override val priceTextView: TextView?
        get() = priceView

    override fun didSetAdContent(adContent: PWNativeViewContent) {
        val params = mediaContainer.layoutParams
        params.height = if (adContent.mediaAspectRatio != null) {
            ViewGroup.LayoutParams.WRAP_CONTENT
        } else {
            220
        }
        mediaContainer.layoutParams = params
        requestLayout()
    }
}