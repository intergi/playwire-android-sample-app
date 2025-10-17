package com.example.demo_compose.ads.view.nativead

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import com.example.demo_compose.R
import com.example.demo_compose.misc.Constant
import com.example.demo_compose.ui.theme.PlaywireAppTheme
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.intergi.playwiresdk.ads.view.nativead.PWNativeView
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewFactory

class NativeAdActivity : ComponentActivity() {

    private var nativeAd: PWNativeView? = null
    private lateinit var adUnitName: String
    private val statusText = mutableStateOf("Ad status will be displayed here")
    private val isAdLoaded = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""

        setContent {
            PlaywireAppTheme {
                NativeAdScreen(adUnitName, statusText.value)  { onBackPressedDispatcher.onBackPressed() }
            }
        }

        loadNativeAd()
    }

    private fun loadNativeAd() {
        val factory = object : PWNativeViewFactory {
            override fun createAdContentView(nativeView: PWNativeView, adContent: PWNativeViewContent): View {
                return ComposeView(this@NativeAdActivity).apply {
                    setContent {
                        NativeAdView(adContent)
                    }
                }
            }

            override fun callToActionView(nativeView: PWNativeView, adContentView: View): View? {
                return adContentView.findViewWithTag("call_to_action")
            }
        }

        val listener = object : PWViewAd.Listener {
            override fun onViewAdLoaded(ad: PWViewAd) {
                statusText.value = getString(R.string.native_ad_loaded, adUnitName)
                isAdLoaded.value = true
            }

            override fun onViewAdFailedToLoad(ad: PWViewAd) {
                statusText.value = getString(R.string.native_ad_load_failed, adUnitName)
            }
        }

        nativeAd = PWNativeView(this, adUnitName, factory, listener)
        nativeAd?.load()

        statusText.value = getString(R.string.native_ad_loading, adUnitName)
    }

    override fun onDestroy() {
        nativeAd?.destroy()
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NativeAdScreen(
    adUnitName: String?,
    statusText: String,
    onNavigateUp: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = adUnitName ?: "Native Ad") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp }) {
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
        }
    }
}

@Composable
fun NativeAdView(adContent: PWNativeViewContent) {
    Card(
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                adContent.icon?.let {
                    Image(bitmap = it.toBitmap().asImageBitmap(), contentDescription = null, modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    adContent.headline?.let { Text(text = it, style = MaterialTheme.typography.headlineSmall) }
                    adContent.advertiser?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            adContent.body?.let { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
            Spacer(modifier = Modifier.height(8.dp))
            adContent.mediaView?.let {
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp)
                    ,
                    factory = { context -> it }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                adContent.price?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
                Spacer(modifier = Modifier.width(8.dp))
                adContent.store?.let { Text(text = it, style = MaterialTheme.typography.bodySmall) }
                Spacer(modifier = Modifier.width(8.dp))
                adContent.callToAction?.let {
                    AndroidView(
                        factory = { context ->
                            android.widget.Button(context).apply {
                                text = it
                                tag = "call_to_action"
                            }
                        }
                    )
                }
            }
        }
    }
}
