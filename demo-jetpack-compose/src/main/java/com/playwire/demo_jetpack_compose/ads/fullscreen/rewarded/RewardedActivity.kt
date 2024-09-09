package com.playwire.demo_jetpack_compose.ads.fullscreen.rewarded

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.intergi.playwiresdk.PWAdMode
import com.playwire.demo_jetpack_compose.ads.fullscreen.FullScreenAdState
import com.playwire.demo_jetpack_compose.ads.fullscreen.FullScreenAdStatus
import com.playwire.demo_jetpack_compose.misc.Constant
import com.playwire.demo_jetpack_compose.misc.RootScreenWithTopBar

class RewardedActivity : ComponentActivity() {
    private lateinit var viewModel: RewardedViewModel
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        viewModel = RewardedViewModel(this, adUnitName)

        viewModel.loadRewarded()

        setContent {

            val state = viewModel.state.observeAsState(initial = FullScreenAdState.Loading)

            RootScreenWithTopBar {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = it.fillMaxSize()
                ) {
                    FullScreenAdStatus(state = state.value, mode = "rewarded", adUnitName = adUnitName)
                    Button(
                        onClick = {
                            if (state.value == FullScreenAdState.Loaded) {
                                viewModel.showRewarded()
                            }
                        },
                        enabled = state.value == FullScreenAdState.Loaded
                    ) {
                        Text(text = "Show Rewarded")
                    }
                }
            }
        }
    }
}