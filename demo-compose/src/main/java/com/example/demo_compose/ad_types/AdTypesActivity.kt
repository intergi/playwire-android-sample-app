package com.example.demo_compose.ad_types

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demo_compose.BuildConfig
import com.example.demo_compose.ads.fullscreen.app_open.AppOpenAdActivity
import com.example.demo_compose.ads.fullscreen.interstitial.InterstitialActivity
import com.example.demo_compose.ads.fullscreen.rewarded.RewardedActivity
import com.example.demo_compose.ads.view.banner.BannerActivity
import com.example.demo_compose.ads.view.nativead.NativeAdActivity
import com.example.demo_compose.misc.Constant
import com.example.demo_compose.ui.theme.PlaywireAppTheme
import com.intergi.playwiresdk.PWAdMode
import com.intergi.playwiresdk.PWNotifier
import com.intergi.playwiresdk.PlaywireSDK

class AdTypesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaywireAppTheme {
                AdTypesScreen(this)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdTypesScreen(activity: Activity) {
    val context = LocalContext.current
    val adUnits = remember { mutableStateListOf<Pair<PWAdMode, String>>() }

    LaunchedEffect(Unit) {
        if (BuildConfig.DEBUG) {
            PWNotifier.startConsoleLogger()
            PlaywireSDK.test = true
        }
        PlaywireSDK.initialize("1024407", "703", activity) {
            adUnits.addAll(PlaywireSDK.getConfig()?.adUnits?.map {
                Pair(it.mode, it.name)
            } ?: emptyList())
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            TopAppBar(title = { Text("Ad Types") })
            LazyColumn {
                items(adUnits) { adUnit ->
                    AdUnitRow(adUnit = adUnit) {
                        val activityClass = when (adUnit.first) {
                            PWAdMode.Banner -> BannerActivity::class.java
                            PWAdMode.Interstitial -> InterstitialActivity::class.java
                            PWAdMode.Rewarded -> RewardedActivity::class.java
                            PWAdMode.AppOpenAd -> AppOpenAdActivity::class.java
                            PWAdMode.Native -> NativeAdActivity::class.java
                            else -> throw IllegalArgumentException("Invalid ad mode: ${adUnit.first}")
                        }
                        val intent = Intent(context, activityClass)
                        intent.putExtra(Constant.adUnitNameKey, adUnit.second)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun AdUnitRow(adUnit: Pair<PWAdMode, String>, onAdUnitClick: () -> Unit) {
    Text(
        text = adUnit.second,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAdUnitClick() }
            .padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlaywireAppTheme {
        AdTypesScreen(ComponentActivity())
    }
}
