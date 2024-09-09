package com.playwire.demo_jetpack_compose.ads.fullscreen.appopenad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.playwire.demo_jetpack_compose.ads.fullscreen.FullScreenAdState
import com.playwire.demo_jetpack_compose.ads.fullscreen.FullScreenAdStatus
import com.playwire.demo_jetpack_compose.misc.Constant
import com.playwire.demo_jetpack_compose.misc.RootScreenWithTopBar

class AppOpenAdActivity : ComponentActivity() {
    private lateinit var viewModel: AppOpenAdViewModel
    // The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    private lateinit var adUnitName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        viewModel = AppOpenAdViewModel(this, adUnitName)

        setContent {
            LaunchedEffect(Unit) { viewModel.loadAppOpenAd() }
            DisposableEffectWithLifeCycle(
                onStart = { viewModel.onStartStateChanged() },
                onPause = { viewModel.onPauseStateChanged() }
            ) {
                val state = viewModel.state.observeAsState(initial = FullScreenAdState.Loading)

                RootScreenWithTopBar {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = it.fillMaxSize()
                    ) {
                        FullScreenAdStatus(state = state.value, mode = "app open ad", adUnitName = adUnitName)
                        Text(
                            text = "\nGo to Home screen and open the app again to see the app open ad.\n\nOR\n",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Button(
                            onClick = { viewModel.showAppOpenAd() },
                            enabled = state.value == FullScreenAdState.Loaded
                        ) {
                            Text(text = "Show App Open Ad")
                        }
                    }
                }
            }
        }
    }
}


// Create the composable to observe application life cycle
@Composable
private fun DisposableEffectWithLifeCycle(
    onStart: () -> Unit,
    onPause: () -> Unit,
    content: @Composable () -> Unit
) {
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnPause by rememberUpdatedState(onPause)

    // Safely update the current lambdas when a new one is provided
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    // If `lifecycleOwner` changes, dispose and reset the effect
    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for lifecycle events
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    currentOnStart()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    currentOnPause()
                }
                else -> {}
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    content()
}