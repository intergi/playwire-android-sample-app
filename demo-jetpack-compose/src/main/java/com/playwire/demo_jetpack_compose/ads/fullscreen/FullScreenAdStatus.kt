package com.playwire.demo_jetpack_compose.ads.fullscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FullScreenAdStatus(state: FullScreenAdState, mode: String, adUnitName: String) {
    Text(
        text = when (state) {
            FullScreenAdState.Loading -> "⏳ The $mode \"$adUnitName\" is loading."
            FullScreenAdState.Loaded -> "✅ The $mode \"$adUnitName\" is loaded."
            FullScreenAdState.Failed -> "❌ Failed to load the $mode \"$adUnitName\"."
            FullScreenAdState.FailedToShow -> "❌ Failed to show the $mode \"$adUnitName\"."
            FullScreenAdState.Shown -> "👍 The $mode \"$adUnitName\" was successfully shown."
            is FullScreenAdState.EarnedReward -> "🎉 The reward is earned.\n Type: ${state.type} \n Amount: ${state.amount}"
        },
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}