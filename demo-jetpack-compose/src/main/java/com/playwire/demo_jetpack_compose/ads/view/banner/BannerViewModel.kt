package com.playwire.demo_jetpack_compose.ads.view.banner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intergi.playwiresdk.PlaywireSDK
import com.intergi.playwiresdk.ads.view.PWViewAd
import com.playwire.demo_jetpack_compose.ads.view.ViewAdState

class BannerViewModel(val adUnitName: String) : ViewModel(), PWViewAd.Listener {
    private val _state = MutableLiveData<ViewAdState>(ViewAdState.Loading)
    val state: LiveData<ViewAdState> = _state
    private var isRefreshing = false

    override fun onViewAdLoaded(ad: PWViewAd) {
        _state.value = ViewAdState.Loaded
        isRefreshing = false
    }

    override fun onViewAdFailedToLoad(ad: PWViewAd) {
        _state.value = ViewAdState.Failed
        isRefreshing = false
    }

    override fun onViewAdOpened(ad: PWViewAd) {
    }

    override fun onViewAdClosed(ad: PWViewAd) {
    }

    override fun onViewAdClicked(ad: PWViewAd) {
    }

    override fun onViewAdImpression(ad: PWViewAd) {
    }

    fun refresh() {
        if (isRefreshing && state.value == ViewAdState.Refreshing) return

        // Refresh will start only if the ad unit contains `refresh` object.
        // See logs from `PWNotifier` to track status of refresh.

        val adUnit = PlaywireSDK.getConfig()?.adUnits?.firstOrNull { it.name == adUnitName }
        val refresh = adUnit?.refresh
        if (refresh == null) {
            _state.value = ViewAdState.CanNotBeRefreshed
            return
        }
        isRefreshing = true
        _state.value = ViewAdState.Refreshing
    }
}