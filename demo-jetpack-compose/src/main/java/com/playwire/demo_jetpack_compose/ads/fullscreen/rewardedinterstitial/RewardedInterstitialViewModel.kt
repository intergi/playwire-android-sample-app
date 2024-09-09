package com.playwire.demo_jetpack_compose.ads.fullscreen.rewardedinterstitial

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd
import com.intergi.playwiresdk.ads.fullscreen.rewardedinterstitial.PWRewardedInterstitial
import com.playwire.demo_jetpack_compose.ads.fullscreen.FullScreenAdState

class RewardedInterstitialViewModel(activity: Activity, adUnitName: String) : ViewModel() {
    private val _state = MutableLiveData<FullScreenAdState>(FullScreenAdState.Loading)
    val state: LiveData<FullScreenAdState> = _state

    private var rewardedInterstitialAd: PWRewardedInterstitial

    init {

        val listener = object: PWFullScreenAd.Listener {
            override fun onFullScreenAdLoaded(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.Loaded
            }

            override fun onFullScreenAdFailedToLoad(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.Failed
            }

            override fun onFullScreenAdFailedToShowFullScreenContent(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.FailedToShow
            }

            override fun onFullScreenAdShowedFullScreenContent(ad: PWFullScreenAd) {
                _state.value = FullScreenAdState.Shown
            }

            override fun onFullScreenAdReward(ad: PWFullScreenAd, type: String, amount: Int) {
                _state.value = FullScreenAdState.EarnedReward(type, amount)
            }
        }

        rewardedInterstitialAd = PWRewardedInterstitial(activity, adUnitName, listener)
    }

    fun loadRewardedInterstitial() {
        // Use `PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        // val params = PWLoadParams().withTargeting(
        //   mapOf(
        //      "age" to "18-32",
        //      "page" to "travel"
        //   )
        // )
        // rewardedInterstitialAd.load(params)
        rewardedInterstitialAd.load()
    }

    fun showRewardedInterstitial() {
        if (rewardedInterstitialAd.isLoaded) {
            rewardedInterstitialAd.show()
        } else {
            // Load rewarded interstitialAd one more time or notify a user about error.
        }
    }
}