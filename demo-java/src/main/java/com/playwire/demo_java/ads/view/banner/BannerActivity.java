package com.playwire.demo_java.ads.view.banner;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.intergi.playwiresdk.PWAdUnit;
import com.intergi.playwiresdk.PlaywireSDK;
import com.intergi.playwiresdk.ads.view.PWViewAd;
import com.intergi.playwiresdk.ads.view.banner.PWBannerView;
import com.playwire.demo_java.R;
import com.playwire.demo_java.misc.Constant;

public class BannerActivity extends AppCompatActivity {

    PWBannerView bannerView;
    /// The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    String adUnitName;
    Boolean isBannerAdded = false;
    TextView statusTextView;
    Button refreshButton;

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
        setContentView(R.layout.activity_banner);

        adUnitName = getIntent().getStringExtra(Constant.adUnitNameKey);
        statusTextView = findViewById(R.id.status_text_view);
        refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        loadBanner();
    }

    void loadBanner() {
        PWViewAd.Listener listener = new PWViewAd.Listener() {
            @Override
            public void onViewAdLoaded(@NonNull PWViewAd pwViewAd) {
                statusTextView.setText(String.format("✅ The banner \"%s\" is loaded.", adUnitName));

                addBannerToParent();
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

        bannerView = new PWBannerView(this, adUnitName, listener);

        /// Use `new PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        /// PWLoadParams params = new PWLoadParams().withTargeting(new HashMap<String, String>() {{
        ///   put("age", "18-35");
        ///   put("page", "travel");
        /// }});
        /// bannerView.load(params);

        bannerView.load();
        statusTextView.setText(String.format("⏳ The banner \"%s\" is loading.", adUnitName));
    }

    private void refresh() {
        /// Refresh will start only if the ad unit contains `refresh` object.
        /// See logs from `PWNotifier` to track status of refresh.
        if (bannerView == null) return;
        bannerView.refresh();


        PWAdUnit adUnit = null;
        for (PWAdUnit item: PlaywireSDK.INSTANCE.getConfig().getAdUnits()) {
            String name = item.getName();
            if (name != null && name.equals(adUnitName)) {
                adUnit = item;
                break;
            }
        }
        if (adUnit.getRefresh() == null) {
            statusTextView.setText(String.format("⚠️ The banner \"%s\" can't be refreshed manually.\nSee logs to get more details.", adUnitName));
            return;
        }
        statusTextView.setText(String.format("🔄 The banner \"%s\" is refreshing.", adUnitName));
    }

    private void addBannerToParent() {
        if (isBannerAdded || bannerView == null) return;
        isBannerAdded = true;

        /// Banner is ready to be added to view hierarchy
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        RelativeLayout container = findViewById(R.id.banner_container);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
        container.addView(bannerView, layoutParams);
    }
}