package com.example.demo_compose.ads.view.nativead

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.demo_compose.R
import com.example.demo_compose.misc.Constant
import com.example.demo_compose.ui.theme.PlaywireAppTheme
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.nativead.PWNativeView
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContentView
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewFactory

class NativeAdActivity : ComponentActivity() {

    private var nativeAdView: PWNativeView? = null
    private lateinit var nativeAdHost: FrameLayout
    private lateinit var adUnitName: String
    private var statusText by mutableStateOf("Ad status will be displayed here")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""

        nativeAdHost = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        setContent {
            PlaywireAppTheme {
                NativeAdScreen(
                    adUnitName = adUnitName,
                    statusText = statusText,
                    nativeAdHost = nativeAdHost,
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
                nativeAdView?.visibility = View.VISIBLE
                nativeAdHost.visibility = View.VISIBLE
                nativeAdHost.requestLayout()
            }

            override fun onViewAdFailedToLoad(ad: PWViewAd) {
                statusText = getString(R.string.native_ad_load_failed, adUnitName)
                nativeAdView?.visibility = View.GONE
                nativeAdHost.visibility = View.GONE
                nativeAdHost.requestLayout()
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

        nativeAdView = PWNativeView(this, adUnitName, factory, listener).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        nativeAdHost.removeAllViews()
        nativeAdHost.addView(nativeAdView)

        nativeAdView?.visibility = View.GONE
        nativeAdHost.visibility = View.GONE

        nativeAdView?.load()
        statusText = getString(R.string.native_ad_loading, adUnitName)
    }

    override fun onDestroy() {
        nativeAdView?.destroy()
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NativeAdScreen(
    adUnitName: String,
    statusText: String,
    nativeAdHost: FrameLayout,
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(text = statusText)

            Spacer(modifier = Modifier.height(16.dp))

            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { nativeAdHost },
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

private class ComposeNativeContentView(context: Context) :
    LinearLayout(context), PWNativeViewContentView {

    private val attributionView = TextView(context).apply {
        text = "Ad"
        textSize = 14f
        setPadding(dp(4), dp(2), dp(4), dp(2))
    }

    private val iconView = ImageView(context).apply {
        layoutParams = LayoutParams(dp(40), dp(40))
    }

    private val headlineView = TextView(context).apply {
        textSize = 16f
    }

    private val advertiserView = TextView(context).apply {
        textSize = 14f
    }

    private val ratingView = FrameLayout(context).apply {
        layoutParams = LayoutParams(0, dp(20), 1f)
        minimumWidth = dp(20)
    }

    private val priceView = TextView(context).apply {
        textSize = 12f
        setPadding(dp(5), 0, dp(5), 0)
    }

    private val storeView = TextView(context).apply {
        textSize = 12f
        setPadding(dp(5), 0, dp(5), 0)
    }

    private val bodyView = TextView(context).apply {
        textSize = 12f
    }

    private val mediaContainer = FrameLayout(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(5)
        }
    }

    private val ctaButton = Button(context).apply {
        textSize = 12f
    }

    init {
        orientation = VERTICAL
        layoutParams = ViewGroup.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )

        addView(
            attributionView,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        )

        val headerRow = LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(8)
            }
        }

        val textColumn = LinearLayout(context).apply {
            orientation = VERTICAL
            layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f).apply {
                marginStart = dp(8)
                marginEnd = dp(8)
            }
        }

        val metaRow = LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        }

        metaRow.addView(ratingView)
        metaRow.addView(priceView)
        metaRow.addView(storeView)

        textColumn.addView(headlineView)
        textColumn.addView(advertiserView)
        textColumn.addView(metaRow)

        headerRow.addView(iconView)
        headerRow.addView(textColumn)

        addView(headerRow)

        addView(
            bodyView,
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(8)
            }
        )

        addView(mediaContainer)

        addView(
            ctaButton,
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
        )
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

    override val starRatingView: ViewGroup
        get() = ratingView

    override val storeTextView: TextView?
        get() = storeView

    override val priceTextView: TextView?
        get() = priceView

    override fun didSetAdContent(adContent: PWNativeViewContent) {
        val params = mediaContainer.layoutParams
        params.height = if (adContent.mediaAspectRatio == null) {
            dp(120)
        } else {
            LayoutParams.WRAP_CONTENT
        }
        mediaContainer.layoutParams = params
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}