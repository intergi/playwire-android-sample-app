package com.playwire.demo_java.ads.fullscreen.rewardedinterstitial;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd;
import com.intergi.playwiresdk.ads.fullscreen.rewardedinterstitial.PWRewardedInterstitial;
import com.playwire.demo_java.R;
import com.playwire.demo_java.misc.Constant;

public class RewardedInterstitialActivity extends AppCompatActivity {
    PWRewardedInterstitial rewardedInterstitial;
    /// The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    String adUnitName;
    TextView statusTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded_interstitial);

        adUnitName = getIntent().getStringExtra(Constant.adUnitNameKey);
        statusTextView = findViewById(R.id.status_text_view);
        loadRewardedInterstitial();
    }

    void loadRewardedInterstitial() {
        PWFullScreenAd.Listener listener = new PWFullScreenAd.Listener() {
            @Override
            public void onFullScreenAdLoaded(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("✅ The rewarded interstitial \"%s\" is loaded.", adUnitName));

                showRewardedInterstitial();
            }

            @Override
            public void onFullScreenAdFailedToLoad(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("❌ Failed to load the rewarded interstitial \"%s\".", adUnitName));
            }

            @Override
            public void onFullScreenAdShowedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("👍 The rewarded interstitial \"%s\" was successfully shown.", adUnitName));
            }

            @Override
            public void onFullScreenAdFailedToShowFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("❌ Failed to show the rewarded interstitial \"%s\".", adUnitName));
            }

            @Override
            public void onFullScreenAdDismissedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
            }

            @Override
            public void onFullScreenAdImpression(@NonNull PWFullScreenAd pwFullScreenAd) {
            }

            @Override
            public void onFullScreenAdReward(@NonNull PWFullScreenAd pwFullScreenAd, @NonNull String type, int amount) {
                // Handle a reward regarding your business objectives.
                statusTextView.setText(String.format("🎉 The reward is earned.\n Type: %s \n Amount: %d", type, amount));
            }
        };

        rewardedInterstitial = new PWRewardedInterstitial(this, adUnitName, listener);

        /// Use `new PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        /// PWLoadParams params = new PWLoadParams().withTargeting(new HashMap<String, String>() {{
        ///   put("age", "18-35");
        ///   put("page", "travel");
        /// }});
        /// rewardedInterstitial.load(params);

        rewardedInterstitial.load();
        statusTextView.setText(String.format("⏳ The rewarded interstitial \"%s\" is loading.", adUnitName));
    }

    void showRewardedInterstitial() {
        if (rewardedInterstitial == null || !rewardedInterstitial.isLoaded()) {
            /// Load rewarded interstitial one more time or notify a user about error.
            return;
        }
        rewardedInterstitial.show();
    }
}
