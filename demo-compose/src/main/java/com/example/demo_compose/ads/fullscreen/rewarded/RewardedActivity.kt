package com.example.demo_compose.ads.fullscreen.rewarded

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
import com.example.demo_compose.R
import com.example.demo_compose.misc.Constant
import com.example.demo_compose.ui.theme.PlaywireAppTheme
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.rewarded.PWRewarded

class RewardedActivity : ComponentActivity() {

    private var rewarded: PWRewarded? = null
    private lateinit var adUnitName: String
    private val statusText = mutableStateOf("Ad status will be displayed here")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""

        setContent {
            PlaywireAppTheme {
                RewardedAdScreen(adUnitName, statusText.value) { onBackPressedDispatcher.onBackPressed() }
            }
        }

        loadAd()
    }

    private fun loadAd() {
        val adUnitName = adUnitName ?: return
        val listener = object : PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                statusText.value = getString(R.string.rewarded_ad_loaded, adUnitName)
                showAd()
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {
                statusText.value = getString(R.string.rewarded_ad_load_failed, adUnitName)
            }

            override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {
                statusText.value = getString(R.string.rewarded_ad_shown, adUnitName)
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {
                statusText.value = getString(R.string.rewarded_ad_show_failed, adUnitName)
            }

            override fun onFullScreenAdReward(ad: PWFullScreenAd, type: String, amount: Int) {
                statusText.value = getString(R.string.rewarded_ad_earned, adUnitName)
            }
        }

        rewarded = PWRewarded(this, adUnitName, listener)
        rewarded?.load()

        statusText.value = getString(R.string.rewarded_ad_loading, adUnitName)
    }

    private fun showAd() {
        val rewarded = rewarded ?: return
        if (rewarded.isLoaded) {
            rewarded.show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardedAdScreen(adUnitName: String?, statusText: String, onNavigateUp: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = adUnitName ?: "Rewarded Ad") },
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