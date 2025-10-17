package com.example.demo_java.ads.fullscreen.app_open;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.demo_java.R;
import com.example.demo_java.ads.fullscreen.FullScreenAdActivity;
import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd;
import com.intergi.playwiresdk.ads.fullscreen.appopen.PWAppOpenAd;

public class AppOpenAdActivity extends FullScreenAdActivity implements LifecycleEventObserver {
    PWAppOpenAd appOpenAd;

    @Override
    protected void onStart() {
        super.onStart();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
    }

    /// Observe an app state to show the ad when a user open the app.
    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_START:
                loadAd();
                break;
            case ON_PAUSE:
                // Check if we need to load app open ad before next presentation
                if (appOpenAd != null && appOpenAd.isLoaded()) return;
                loadAd();
                break;
            default: break;
        }
    }

    @Override
    protected void loadAd() {
        PWFullScreenAd.Listener listener = new PWFullScreenAd.Listener() {
            @Override
            public void onFullScreenAdReward(@NonNull PWFullScreenAd pwFullScreenAd, @NonNull String s, int i) {

            }

            @Override
            public void onFullScreenAdImpression(@NonNull PWFullScreenAd pwFullScreenAd) {

            }

            @Override
            public void onFullScreenAdDismissedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {

            }

            @Override
            public void onFullScreenAdLoaded(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(getString(R.string.app_open_ad_loaded, adUnitName));
                showAd();
            }

            @Override
            public void onFullScreenAdFailedToLoad(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(getString(R.string.app_open_ad_load_failed, adUnitName));
            }

            @Override
            public void onFullScreenAdShowedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(getString(R.string.app_open_ad_shown, adUnitName));
            }

            @Override
            public void onFullScreenAdFailedToShowFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(getString(R.string.app_open_ad_show_failed, adUnitName));
            }
        };

        appOpenAd = new PWAppOpenAd(getApplication(), adUnitName, listener);

        /// Ads rendered more than four hours after request time will no longer be valid and may not earn revenue.
        /// Enable the property below to start loading new ad automatically if more than a certain number of hours have passed since your ad loaded.
        /// It equals to `false` by default.
        appOpenAd.setAutoReloadOnExpiration(true);

//        PWLoadParams params = new PWLoadParams()
//                .withDeviceOrientation(Configuration.ORIENTATION_PORTRAIT)
//                        .withTargeting(new HashMap<>() {{
//                            put("age", "18-35");
//                            put("page", "travel");
//                        }});
//        appOpenAd.load(params);

        appOpenAd.load();

        statusTextView.setText(getString(R.string.app_open_ad_loading));
    }

    @Override
    protected void showAd() {
        if (appOpenAd == null || !appOpenAd.isLoaded()) {
            /// Load app open ad one more time or notify a user about error.
            return;
        }
        appOpenAd.show(this);
    }
}