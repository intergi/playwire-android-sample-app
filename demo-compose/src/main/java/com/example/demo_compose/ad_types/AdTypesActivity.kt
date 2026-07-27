package com.example.demo_compose.ad_types

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.demo_compose.ads.fullscreen.app_open.AppOpenAdActivity
import com.example.demo_compose.ads.fullscreen.interstitial.InterstitialActivity
import com.example.demo_compose.ads.fullscreen.rewarded.RewardedActivity
import com.example.demo_compose.ads.view.banner.BannerActivity
import com.example.demo_compose.ads.view.nativead.NativeAdActivity
import com.example.demo_compose.misc.Constant
import com.example.demo_compose.ui.theme.PlaywireAppTheme
import com.intergi.playwiresdk.PlaywireSDK
import com.intergi.playwiresdk.logger.LogLevel

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
    val isInitialized = remember { mutableStateOf(false) }

    val adUnits: List<Pair<String, Class<out Activity>>> = listOf(
        Pair("banner-320x50-gam", BannerActivity::class.java),
        Pair("banner-320x50-max", BannerActivity::class.java),
        Pair("banner-300x250-gam", BannerActivity::class.java),
        Pair("banner-300x250-max", BannerActivity::class.java),
        Pair("native-gam", NativeAdActivity::class.java),
        Pair("native-max", NativeAdActivity::class.java),
        Pair("app-open-gam", AppOpenAdActivity::class.java),
        Pair("app-open-max", AppOpenAdActivity::class.java),
        Pair("interstitial-gam", InterstitialActivity::class.java),
        Pair("interstitial-max", InterstitialActivity::class.java),
        Pair("rewarded-gam", RewardedActivity::class.java),
        Pair("rewarded-video-max", RewardedActivity::class.java),
        Pair("floating-banner", BannerActivity::class.java)
    )

    LaunchedEffect(Unit) {
        PlaywireSDK.setLogLevel(LogLevel.INFO)
        PlaywireSDK.test = false

        PlaywireSDK.start("1024407", "703", activity) { success, error ->
            if (success) {
                isInitialized.value = true
            } else {
                Toast.makeText(
                    context,
                    error?.message ?: "Playwire SDK initialization failed.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            TopAppBar(title = { Text("demo-compose") })
            LazyColumn {
                items(adUnits) { (adUnitName, activityClass) ->
                    AdUnitRow(adUnitName = adUnitName) {
                        val intent = Intent(context, activityClass)
                        intent.putExtra(Constant.adUnitNameKey, adUnitName)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun AdUnitRow(adUnitName: String, onAdUnitClick: () -> Unit) {
    Text(
        text = adUnitName,
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