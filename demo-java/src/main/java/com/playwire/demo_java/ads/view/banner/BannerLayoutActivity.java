package com.playwire.demo_java.ads.view.banner;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.intergi.playwiresdk.ads.view.PWViewAd;
import com.intergi.playwiresdk.ads.view.banner.PWBannerView;
import com.playwire.demo_java.R;

public class BannerLayoutActivity extends AppCompatActivity {

    PWBannerView bannerView;
    TextView statusTextView;
    /// The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    String adUnitName = "Banner-300x250";

    @Override
    protected void onDestroy() {
        /// Must call the `destroy` method to avoid memory leak
        if (bannerView != null) {
            bannerView.destroy();
        }

        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_layout);

        statusTextView = findViewById(R.id.status_text_view);

        PWViewAd.Listener listener = new PWViewAd.Listener() {
            @Override
            public void onViewAdLoaded(@NonNull PWViewAd pwViewAd) {
                statusTextView.setText(String.format("✅ The banner \"%s\" is loaded.", adUnitName));
            }

            @Override
            public void onViewAdFailedToLoad(@NonNull PWViewAd pwViewAd) {
                statusTextView.setText(String.format("❌ Failed to load the banner \"%s\".", adUnitName));
            }

            @Override
            public void onViewAdOpened(@NonNull PWViewAd pwViewAd) {
            }

            @Override
            public void onViewAdClosed(@NonNull PWViewAd pwViewAd) {
            }

            @Override
            public void onViewAdClicked(@NonNull PWViewAd pwViewAd) {
            }

            @Override
            public void onViewAdImpression(@NonNull PWViewAd pwViewAd) {
            }
        };

        bannerView = findViewById(R.id.static_banner_view);
        bannerView.setListener(listener);
        statusTextView.setText(String.format("⏳ The banner \"%s\" is loading.", adUnitName));
    }
}
