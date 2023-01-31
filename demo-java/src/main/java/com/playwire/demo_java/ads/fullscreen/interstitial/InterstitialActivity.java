package com.playwire.demo_java.ads.fullscreen.interstitial;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd;
import com.intergi.playwiresdk.ads.fullscreen.interstitial.PWInterstitial;
import com.playwire.demo_java.R;
import com.playwire.demo_java.misc.Constant;

public class InterstitialActivity extends AppCompatActivity {

    PWInterstitial interstitial;
    /// The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    String adUnitName;
    TextView statusTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        adUnitName = getIntent().getStringExtra(Constant.adUnitNameKey);
        statusTextView = findViewById(R.id.status_text_view);
        loadInterstitial();
    }

    void loadInterstitial() {
        PWFullScreenAd.Listener listener = new PWFullScreenAd.Listener() {
            @Override
            public void onFullScreenAdLoaded(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("✅ The interstitial \"%s\" is loaded.", adUnitName));

                showInterstitial();
            }

            @Override
            public void onFullScreenAdFailedToLoad(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("❌ Failed to load the interstitial \"%s\".", adUnitName));
            }

            @Override
            public void onFullScreenAdShowedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
            }

            @Override
            public void onFullScreenAdFailedToShowFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("❌ Failed to show the interstitial \"%s\".", adUnitName));
            }

            @Override
            public void onFullScreenAdDismissedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("👍 The interstitial \"%s\" was successfully shown.", adUnitName));
            }

            @Override
            public void onFullScreenAdImpression(@NonNull PWFullScreenAd pwFullScreenAd) {
            }

            @Override
            public void onFullScreenAdReward(@NonNull PWFullScreenAd pwFullScreenAd, @NonNull String s, int i) {
            }
        };

        this.interstitial = new PWInterstitial(this, adUnitName, listener);

        /// Use `new PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        /// PWLoadParams params = new PWLoadParams().withTargeting(new HashMap<String, String>() {{
        ///   put("age", "18-35");
        ///   put("page", "travel");
        /// }});
        /// this.interstitial.load(params);

        this.interstitial.load();
        statusTextView.setText(String.format("⏳ The interstitial \"%s\" is loading.", adUnitName));

    }

    void showInterstitial() {
        if (interstitial == null || !interstitial.isLoaded()) {
            /// Load interstitial one more time or notify a user about error
            return;
        }
        interstitial.show();
    }
}
