package com.playwire.demo_jetpack_compose.ads.view.banner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.intergi.playwiresdk.ads.view.banner.PWBannerView
import com.playwire.demo_jetpack_compose.ads.view.ViewAdState
import com.playwire.demo_jetpack_compose.ads.view.ViewAdStatus
import com.playwire.demo_jetpack_compose.misc.Constant
import com.playwire.demo_jetpack_compose.misc.RootScreenWithTopBar

class BannerActivity : ComponentActivity() {
    private lateinit var viewModel: BannerViewModel
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        viewModel = BannerViewModel(adUnitName)

        setContent {

            val state = viewModel.state.observeAsState(initial = ViewAdState.Loading)
            RootScreenWithTopBar {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ViewAdStatus(state = state.value, mode = "banner", adUnitName = adUnitName)
                        Button(onClick = { viewModel.refresh() }) { Text(text = "Refresh") }
                    }
                    BannerAdView()
                }
            }
        }
    }

    @Composable
    fun BannerAdView()  {
        val state = viewModel.state.observeAsState()

        // Within factory a default (Activity) context is provided, but here we have to supply an application context to avoid memory leaks.
        val applicationContext = LocalContext.current.applicationContext

        AndroidView(factory = {
            PWBannerView(applicationContext, adUnitName, viewModel).apply {
                // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
                // val params = PWLoadParams().withTargeting(
                //   mapOf(
                //      "age" to "18-32",
                //      "page" to "travel"
                //   )
                // )
                load()
            }
        }, update = {
            if (state.value == ViewAdState.Refreshing) it.refresh()
        })
    }
}