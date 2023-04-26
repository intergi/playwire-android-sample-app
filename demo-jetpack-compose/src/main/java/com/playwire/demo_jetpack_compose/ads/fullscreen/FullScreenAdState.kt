package com.playwire.demo_jetpack_compose.ads.fullscreen

sealed class FullScreenAdState {
    object Loading: FullScreenAdState()
    object Loaded: FullScreenAdState()
    object Failed: FullScreenAdState()
    object FailedToShow: FullScreenAdState()
    object Shown: FullScreenAdState()

    class EarnedReward(val type: String, val amount: Int): FullScreenAdState()
}