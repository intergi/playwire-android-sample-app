package com.playwire.demo_jetpack_compose.ads.view.banner

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.intergi.playwiresdk.ads.PWLoadParams
import com.intergi.playwiresdk.ads.view.banner.PWBannerViewInline
import com.playwire.demo_jetpack_compose.ads.view.ViewAdState
import com.playwire.demo_jetpack_compose.ads.view.ViewAdStatus
import com.playwire.demo_jetpack_compose.misc.Constant
import com.playwire.demo_jetpack_compose.misc.RootScreenWithTopBar

class InlineBannerActivity : ComponentActivity() {
    private lateinit var viewModel: BannerViewModel
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        viewModel = BannerViewModel(adUnitName = adUnitName)

        setContent {
            val state = viewModel.state.observeAsState(initial = ViewAdState.Loading)
            RootScreenWithTopBar {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    ViewAdStatus(state = state.value, mode = "banner", adUnitName = adUnitName)
                    InlineBannerAdView()
                }
            }
        }
    }

    @Composable
    fun InlineBannerAdView()  {

        // Within factory a default (Activity) context is provided, but here we have to supply an application context to avoid memory leaks.
        val applicationContext = LocalContext.current.applicationContext

        AndroidView(factory = {
            PWBannerViewInline(applicationContext, adUnitName, viewModel).apply {
                // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
                // Use `PWLoadParams().withWidth()` to pass available width to fill with ad content.
                // Get the width of the device in use, or set your own width if you don’t want to use the full width of the screen.
                // Use `PWLoadParams().withDeviceOrientation()` to include the orientation of your interface to ad request.
                // Use `ORIENTATION_UNDEFINED` value to allow SDK to determine the orientation.
                // val params = PWLoadParams().withTargeting(
                //   mapOf(
                //      "age" to "18-32",
                //      "page" to "travel"
                //   )
                // )
                // .withWidth(320)
                // .withDeviceOrientation(Configuration.ORIENTATION_UNDEFINED)
                // load(params)
                val params = PWLoadParams()
                    .withWidth(320)
                    .withDeviceOrientation(Configuration.ORIENTATION_UNDEFINED)
                load(params)
            }
        })
    }
}