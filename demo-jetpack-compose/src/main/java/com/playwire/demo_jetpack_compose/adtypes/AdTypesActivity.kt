package com.playwire.demo_jetpack_compose.adtypes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.intergi.playwiresdk.PWAdMode
import com.playwire.demo_jetpack_compose.misc.RootScreenWithTopBar
import com.playwire.demo_jetpack_compose.ui.theme.PlaywireandroidsampleappTheme

class AdTypesActivity : ComponentActivity() {
    private val viewModel = AdTypesViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                viewModel.initialize()
            }
            RootScreenWithTopBar {
                AdTypesScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AdTypesScreen(viewModel: AdTypesViewModel) {
    SDKInitializationStatus(isInitialized = viewModel.isSDKInitialized)
    if (viewModel.isSDKInitialized) {
        AdUnitsList(adUnits = viewModel.adUnits, onClickAction = { viewModel.showAdUnitActivity(it.second, it.first) })
    }
}

@Composable
fun AdUnitsList(adUnits: List<Pair<PWAdMode, String>>, onClickAction: (Pair<PWAdMode, String>) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(adUnits) { adUnit ->
            Box(modifier = Modifier
                .fillMaxSize()
                .clickable { onClickAction(adUnit) }) {
                Column(verticalArrangement = Arrangement.Center, modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)) {
                    Text(text = adUnit.second, fontSize = 15.sp)
                    Text(text = adUnit.first.name, fontSize = 10.sp, maxLines = 1, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}

@Composable
fun SDKInitializationStatus(isInitialized: Boolean) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (!isInitialized) {
            Text(text = "⏳ SDK initializaton...")
        }
    }
}