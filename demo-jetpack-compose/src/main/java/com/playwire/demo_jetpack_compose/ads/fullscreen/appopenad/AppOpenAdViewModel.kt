package com.playwire.demo_jetpack_compose.ads.fullscreen.appopenad

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.appopen.PWAppOpenAd
import com.playwire.demo_jetpack_compose.ads.fullscreen.FullScreenAdState

class AppOpenAdViewModel(private val activity: Activity, private val adUnitName: String) : ViewModel() {
    private val _state = MutableLiveData<FullScreenAdState>(FullScreenAdState.Loading)
    val state: LiveData<FullScreenAdState> = _state

    private var appOpenAd: PWAppOpenAd? = null

    fun loadAppOpenAd() {
        val listener = object: PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.Loaded
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.Failed
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {
                appOpenAd = null
                _state.value = FullScreenAdState.FailedToShow
            }

            override fun onFullScreenAdShowedFullScreenContent(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.Shown
            }

            override fun onFullScreenAdDismissedFullScreenContent(ad: PWFullScreenAd) {
                appOpenAd = null

                // Load app open ad content to be ready for the next presentation.
                loadAppOpenAd()
            }
        }

        appOpenAd = PWAppOpenAd(activity.application, adUnitName, listener)

        // Ads rendered more than four hours after request time will no longer be valid and may not earn revenue.
        // Enable the property below to start loading new ad automatically if more than a certain number of hours have passed since your ad loaded.
        // It equals to `false` by default.
        appOpenAd?.autoReloadOnExpiration = true

        // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        // Use `PWLoadParams().withDeviceOrientation()` to pass the orientation you want to use in the ad request.
        // val params = PWLoadParams()
        // .withDeviceOrientation(Configuration.ORIENTATION_PORTRAIT)
        // .withTargeting(
        //   mapOf(
        //      "age" to "18-32",
        //      "page" to "travel"
        //   )
        // )
        // appOpenAd?.load(params)

        appOpenAd?.load()
    }

    fun showAppOpenAd() {
        val appOpenAd = appOpenAd ?: return
        if (appOpenAd.isLoaded) {
            appOpenAd.show(activity)
        } else {
            // Load app open ad one more time or notify a user about error.
        }
    }

    fun onStartStateChanged() {
        showAppOpenAd()
    }

    fun onPauseStateChanged() {
        // Check if we need to load app open ad before next presentation.
        if (appOpenAd != null && appOpenAd?.isLoaded == true) return
        loadAppOpenAd()
    }
}