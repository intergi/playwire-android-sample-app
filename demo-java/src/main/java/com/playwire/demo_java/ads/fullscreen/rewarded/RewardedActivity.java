package com.playwire.demo_java.ads.fullscreen.rewarded;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.intergi.playwiresdk.ads.fullscreen.PWFullScreenAd;
import com.intergi.playwiresdk.ads.fullscreen.rewarded.PWRewarded;
import com.playwire.demo_java.R;
import com.playwire.demo_java.misc.Constant;

public class RewardedActivity extends AppCompatActivity {
    PWRewarded rewarded;
    /// The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    String adUnitName;

    TextView statusTextView;
    Button getRewardButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded);

        adUnitName = getIntent().getStringExtra(Constant.adUnitNameKey);
        statusTextView = findViewById(R.id.status_text_view);
        getRewardButton = findViewById(R.id.get_reward_button);
        getRewardButton.setEnabled(false);
        getRewardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewarded();
            }
        });

        loadRewarded();
    }

    void loadRewarded() {
        PWFullScreenAd.Listener listener = new PWFullScreenAd.Listener() {
            @Override
            public void onFullScreenAdLoaded(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("✅ The rewarded \"%s\" is loaded.", adUnitName));
                getRewardButton.setEnabled(true);
            }

            @Override
            public void onFullScreenAdFailedToLoad(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("❌ Failed to load the rewarded \"%s\".", adUnitName));
            }

            @Override
            public void onFullScreenAdShowedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("👍 The rewarded \"%s\" was successfully shown.", adUnitName));
                getRewardButton.setEnabled(false);
            }

            @Override
            public void onFullScreenAdFailedToShowFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
                statusTextView.setText(String.format("❌ Failed to show the rewarded \"%s\".", adUnitName));
            }

            @Override
            public void onFullScreenAdDismissedFullScreenContent(@NonNull PWFullScreenAd pwFullScreenAd) {
            }

            @Override
            public void onFullScreenAdImpression(@NonNull PWFullScreenAd pwFullScreenAd) {
            }

            @Override
            public void onFullScreenAdReward(@NonNull PWFullScreenAd pwFullScreenAd, @NonNull String type, int amount) {
                /// Handle a reward regarding your business objectives.
                statusTextView.setText(String.format("🎉 The reward is earned.\n Type: %s \n Amount: %d", type, amount));
            }
        };

        rewarded = new PWRewarded(this, adUnitName, listener);

        /// Use `new PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        /// PWLoadParams params = new PWLoadParams().withTargeting(new HashMap<String, String>() {{
        ///   put("age", "18-35");
        ///   put("page", "travel");
        /// }});
        /// this.rewarded.load(params);

        rewarded.load();
        statusTextView.setText(String.format("⏳ The rewarded \"%s\" is loading.", adUnitName));
    }

    void showRewarded() {
        if (rewarded == null || !rewarded.isLoaded()) {
            /// Load rewarded interstitial one more time or notify a user about error.
            loadRewarded();
            return;
        }
        rewarded.show();
    }
}
