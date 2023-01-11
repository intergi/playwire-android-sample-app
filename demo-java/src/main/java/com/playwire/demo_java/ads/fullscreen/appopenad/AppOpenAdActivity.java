package com.playwire.demo_java.ads.fullscreen.appopenad;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd;
import com.intergi.playwiresdk.ads.fullscreen.appopen.PWAppOpenAd;
import com.playwire.demo_java.R;
import com.playwire.demo_java.misc.Constant;

public class AppOpenAdActivity extends AppCompatActivity implements LifecycleEventObserver {

    PWAppOpenAd appOpenAd;
    /// The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    String adUnitName;
    TextView statusTextView;
    Button showAppOpenAdButton;

    @Override
    protected void onDestroy() {
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_open_ad);

        statusTextView = findViewById(R.id.status_text_view);
        showAppOpenAdButton = findViewById(R.id.show_app_open_ad_button);
        showAppOpenAdButton.setEnabled(false);
        showAppOpenAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAppOpenAd();
            }
        });

        /// Subscribe to listen to an app state.
        /// Make sure that required dependencies are installed.
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        adUnitName = getIntent().getStringExtra(Constant.adUnitNameKey);
        loadAppOpenAd();
    }

    /// Observe an app state to show the ad when a user open the app.
    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_START:
                showAppOpenAd();
                break;
            case ON_PAUSE:
                // Check if we need to load app open ad before next presentation
                if (appOpenAd != null && appOpenAd.isLoaded()) return;
                loadAppOpenAd();
                break;
            default: break;
        }
    }

    void loadAppOpenAd() {
        PWFullScreenAd.Listener listener = new PWFullScreenAd.Listener() {
            @Override
            public void onFullScreenAdLoaded(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("✅ The app open ad \"%s\" is loaded.", adUnitName));
                showAppOpenAdButton.setEnabled(true);
            }

            @Override
            public void onFullScreenAdFailedToLoad(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("❌ Failed to load the app open ad \"%s\".", adUnitName));
                appOpenAd = null;
            }

            @Override
            public void onFullScreenAdShowedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
            }

            @Override
            public void onFullScreenAdFailedToShowFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                appOpenAd = null;
                statusTextView.setText(String.format("❌ Failed to show the app open ad \"%s\".", adUnitName));
            }

            @Override
            public void onFullScreenAdDismissedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                appOpenAd = null;

                /// Load app open ad content to be ready for the next presentation.
                loadAppOpenAd();
            }

            @Override
            public void onFullScreenAdImpression(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("👍 The app open ad \"%s\" was successfully shown.", adUnitName));
                showAppOpenAdButton.setEnabled(false);
            }

            @Override
            public void onFullScreenAdReward(@NonNull PWFullScreenAd pwFullScreenAd, @NonNull String s, int i) {
            }
        };

        appOpenAd = new PWAppOpenAd(getApplication(), adUnitName, listener);

        /// Ads rendered more than four hours after request time will no longer be valid and may not earn revenue.
        /// Enable the property below to start loading new ad automatically if more than a certain number of hours have passed since your ad loaded.
        /// It equals to `false` by default.
        appOpenAd.setAutoReloadOnExpiration(true);

        /// Use `new PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        /// PWLoadParams params = new PWLoadParams()
        /// .withDeviceOrientation(Configuration.ORIENTATION_PORTRAIT)
        /// .withTargeting(new HashMap<String, String>() {{
        ///   put("age", "18-35");
        ///   put("page", "travel");
        /// }});
        /// this.appOpenAd.load(params);

        appOpenAd.load();
        statusTextView.setText(String.format("⏳ The app open ad \"%s\" is loading.", adUnitName));
    }

    void showAppOpenAd() {
        if (appOpenAd == null || !appOpenAd.isLoaded()) {
            /// Load app open ad one more time or notify a user about error.
            return;
        }
        appOpenAd.show(this);
    }
}
