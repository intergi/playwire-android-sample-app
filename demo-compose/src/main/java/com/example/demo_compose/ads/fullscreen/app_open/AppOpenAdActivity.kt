package com.example.demo_compose.ads.fullscreen.app_open

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.demo_compose.misc.Constant
import com.example.demo_compose.ui.theme.PlaywireAppTheme
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.appopen.PWAppOpenAd
import com.example.demo_compose.R

class AppOpenAdActivity : ComponentActivity(), LifecycleEventObserver {

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

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {
                statusText.value = getString(R.string.app_open_ad_load_failed, adUnitName)
            }

            override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {
                statusText.value = getString(R.string.app_open_ad_shown, adUnitName)
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {
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

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                loadAd()
            }

            Lifecycle.Event.ON_PAUSE -> {
                if (appOpenAd != null && appOpenAd?.isLoaded == true) return
                loadAd()
            }

            else -> {}
        }
    }

    override fun onStart() {
        super.onStart()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStop() {
        super.onStop()
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppOpenAdScreen(adUnitName: String?, statusText: String, onNavigateUp: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = adUnitName ?: "App Open Ad") },
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