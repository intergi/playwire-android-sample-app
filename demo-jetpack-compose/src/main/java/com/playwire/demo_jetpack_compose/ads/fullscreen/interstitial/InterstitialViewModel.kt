package com.playwire.demo_jetpack_compose.ads.fullscreen.interstitial

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.interstitial.PWInterstitial
import com.playwire.demo_jetpack_compose.ads.fullscreen.FullScreenAdState

class InterstitialViewModel(activity: Activity, adUnitName: String) : ViewModel() {
    private val _state = MutableLiveData<FullScreenAdState>(FullScreenAdState.Loading)
    val state: LiveData<FullScreenAdState> = _state

    private var interstitialAd: PWInterstitial

    init {

        val listener = object: PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.Loaded
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.Failed
            }

            override fun onFullScreenAdShowedFullScreenContent(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.Shown
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.FailedToShow
            }
        }

        interstitialAd = PWInterstitial(activity, adUnitName, listener)
    }

    fun loadInterstitial() {
        // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        // val params = PWLoadParams().withTargeting(
        //   mapOf(
        //      "age" to "18-32",
        //      "page" to "travel"
        //   )
        // )
        // interstitialAd?.load(params)
        interstitialAd.load()
    }

    fun showInterstitial() {
        if (interstitialAd.isLoaded) {
            interstitialAd.show()
        } else {
            // Load interstitial one more time or notify a user about error.
        }
    }
}