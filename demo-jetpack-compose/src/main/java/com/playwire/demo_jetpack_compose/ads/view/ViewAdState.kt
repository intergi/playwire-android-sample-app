package com.playwire.demo_jetpack_compose.ads.view

sealed class ViewAdState {
    object Loading: ViewAdState()
    object Loaded: ViewAdState()
    object Failed: ViewAdState()
    object CanNotBeRefreshed: ViewAdState()
    object Refreshing: ViewAdState()
}