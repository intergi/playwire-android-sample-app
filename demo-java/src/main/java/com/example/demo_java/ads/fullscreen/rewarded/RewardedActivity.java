package com.example.demo_java.ads.fullscreen.rewarded;

import androidx.annotation.NonNull;

import com.example.demo_java.R;
import com.example.demo_java.ads.fullscreen.FullScreenAdActivity;
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd;
import com.intergi.playwiresdk.ads.fullscreen.rewarded.PWRewarded;

public class RewardedActivity extends FullScreenAdActivity {
    PWRewarded rewarded;

    @Override
    protected void loadAd() {
        PWFullScreenAd.Listener listener = new PWFullScreenAd.Listener() {
            @Override
            public void onFullScreenAdReward(@NonNull PWFullScreenAd pwFullScreenAd, @NonNull String type, int amount) {
                statusTextView.setText(getString(R.string.rewarded_ad_earned, type, amount));
            }

            @Override
            public void onFullScreenAdImpression(@NonNull PWFullScreenAd pwFullScreenAd) {

            }

            @Override
            public void onFullScreenAdDismissedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {

            }

            @Override
            public void onFullScreenAdLoaded(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(getString(R.string.rewarded_ad_loaded, adUnitName));
                showAd();
            }

            @Override
            public void onFullScreenAdFailedToLoad(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(getString(R.string.rewarded_ad_load_failed, adUnitName));
            }

            @Override
            public void onFullScreenAdShowedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(getString(R.string.rewarded_ad_shown, adUnitName));
            }

            @Override
            public void onFullScreenAdFailedToShowFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(getString(R.string.rewarded_ad_show_failed, adUnitName));
            }
        };

        rewarded = new PWRewarded(this, adUnitName, listener);

        /// Ads rendered more than four hours after request time will no longer be valid and may not earn revenue.
        /// Enable the property below to start loading new ad automatically if more than a certain number of hours have passed since your ad loaded.
        /// It equals to `false` by default.

//        PWLoadParams params = new PWLoadParams()
//                .withDeviceOrientation(Configuration.ORIENTATION_PORTRAIT)
//                        .withTargeting(new HashMap<>() {{
//                            put("age", "18-35");
//                            put("page", "travel");
//                        }});
//        interstitial.load(params);

        rewarded.load();

        statusTextView.setText(getString(R.string.interstitial_ad_loading));
    }

    @Override
    protected void showAd() {
        if (rewarded == null || !rewarded.isLoaded()) {
            /// Load app open ad one more time or notify a user about error.
            return;
        }
        rewarded.show();
    }
}