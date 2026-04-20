package com.example.demo_compose.ads.view.banner

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.demo_compose.R
import com.example.demo_compose.misc.Constant
import com.example.demo_compose.ui.theme.PlaywireAppTheme
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.banner.PWBannerView

class BannerActivity : ComponentActivity() {

    private var banner: PWBannerView? = null
    private lateinit var adUnitName: String
    private val statusText = mutableStateOf("Ad status will be displayed here")
    private val isAdLoaded = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""

        setContent {
            PlaywireAppTheme {
                BannerAdScreen(
                    adUnitName = adUnitName,
                    statusText = statusText.value,
                    isAdLoaded = isAdLoaded.value,
                    bannerProvider = { banner },
                    onNavigateUp = { onBackPressedDispatcher.onBackPressed() }
                )
            }
        }

        loadBanner()
    }

    private fun loadBanner() {
        val listener = object : PWViewAd.Listener {
            override fun onViewAdLoaded(ad: PWViewAd) {
                banner?.visibility = View.VISIBLE
                statusText.value = getString(R.string.banner_ad_loaded, adUnitName)
                isAdLoaded.value = true
            }

            override fun onViewAdFailedToLoad(ad: PWViewAd) {
                banner?.visibility = View.GONE
                statusText.value = getString(R.string.banner_ad_load_failed, adUnitName)
                isAdLoaded.value = false
            }
        }

        banner = PWBannerView(this, adUnitName, listener).apply {
            visibility = View.GONE
        }

        statusText.value = getString(R.string.banner_ad_loading, adUnitName)
        isAdLoaded.value = false
        banner?.load()
    }

    override fun onDestroy() {
        banner?.destroy()
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BannerAdScreen(
    adUnitName: String?,
    statusText: String,
    isAdLoaded: Boolean,
    bannerProvider: () -> PWBannerView?,
    onNavigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = adUnitName ?: "Banner Ad") },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = statusText,
                modifier = Modifier.align(Alignment.Center)
            )

            if (isAdLoaded) {
                AndroidView(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    factory = { bannerProvider()!! }
                )
            }
        }
    }
}