package com.example.demo_compose.ads.fullscreen.app_open

import android.os.Bundle
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.demo_compose.R
import com.example.demo_compose.misc.Constant
import com.example.demo_compose.ui.theme.PlaywireAppTheme
import com.intergi.playwiresdk.ads.PWAdError
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.appopen.PWAppOpenAd

class AppOpenAdActivity : ComponentActivity() {

    private lateinit var adUnitName: String
    private var appOpenAd: PWAppOpenAd? = null
    private val statusText = mutableStateOf("Ad status will be displayed here")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""

        setContent {
            PlaywireAppTheme {
                AppOpenAdScreen(adUnitName, statusText.value) { onBackPressedDispatcher.onBackPressed() }
            }
        }

        loadAd()
    }

    private fun loadAd() {
        val listener = object : PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                statusText.value = getString(R.string.app_open_ad_loaded, adUnitName)
                showAd()
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd, error: PWAdError) {
                statusText.value = getString(R.string.app_open_ad_load_failed, adUnitName)
            }

            override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {
                statusText.value = getString(R.string.app_open_ad_shown, adUnitName)
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd, error: PWAdError) {
                statusText.value = getString(R.string.app_open_ad_show_failed, adUnitName)
            }
        }

        appOpenAd = PWAppOpenAd(application, adUnitName, listener)
        appOpenAd?.autoReloadOnExpiration = true
        appOpenAd?.load()

        statusText.value = getString(R.string.app_open_ad_loading, adUnitName)
    }

    private fun showAd() {
        val appOpenAd = appOpenAd ?: return
        if (appOpenAd.isLoaded) {
            appOpenAd.show(this)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppOpenAdScreen(adUnitName: String?, statusText: String, onNavigateUp: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = adUnitName ?: "App Open Ad") },
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
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = statusText)
        }
    }
}