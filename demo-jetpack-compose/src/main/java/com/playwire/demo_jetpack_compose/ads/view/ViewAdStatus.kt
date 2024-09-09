package com.playwire.demo_jetpack_compose.ads.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ViewAdStatus(state: ViewAdState, mode: String, adUnitName: String) {

    Text(
        text = when (state) {
            ViewAdState.Loading -> "⏳ The $mode \"$adUnitName\" is loading."
            ViewAdState.Loaded -> "✅ The $mode \"$adUnitName\" is loaded."
            ViewAdState.Failed -> "❌ Failed to load the $mode \"$adUnitName\"."
            ViewAdState.CanNotBeRefreshed -> "⚠️ The $mode \"$adUnitName\" can't be refreshed manually.\nSee logs to get more details."
            ViewAdState.Refreshing -> "🔄 The $mode \"$adUnitName\" is refreshing."
        },
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}