package com.playwire.demo_java.ads.view.banner;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.intergi.playwiresdk.PWAdUnit;
import com.intergi.playwiresdk.PlaywireSDK;
import com.intergi.playwiresdk.ads.PWLoadParams;
import com.intergi.playwiresdk.ads.view.PWViewAd;
import com.intergi.playwiresdk.ads.view.banner.PWBannerViewInline;
import com.playwire.demo_java.R;
import com.playwire.demo_java.misc.Constant;

public class InlineBannerActivity extends AppCompatActivity {

    PWBannerViewInline bannerView;
    /// The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    String adUnitName;
    Boolean isBannerAdded = false;
    TextView statusTextView;
    Button refreshButton;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /// Must call the `destroy` method to avoid memory leak
        if (bannerView != null) {
            bannerView.destroy();
        }
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

        bannerView = new PWBannerViewInline(this, adUnitName, listener);

        /// Use `new PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        /// Use `new PWLoadParams().withWidth()` to pass available width to fill with ad content.
        /// Get the width of the device in use, or set your own width if you don’t want to use the full width of the screen.
        /// Use `new PWLoadParams().withDeviceOrientation()` to include the orientation of your interface to ad request.
        /// Use `ORIENTATION_UNDEFINED` value to allow SDK to determine the orientation.
        /// PWLoadParams params = new PWLoadParams().withTargeting(new HashMap<String, String>() {{
        ///   put("age", "18-35");
        ///   put("page", "travel");
        /// }})
        /// .withWidth(320)
        /// .withDeviceOrientation(Configuration.ORIENTATION_UNDEFINED);
        /// bannerView.load(params);

        PWLoadParams params = new PWLoadParams()
                .withWidth(320)
                .withDeviceOrientation(Configuration.ORIENTATION_PORTRAIT);
        bannerView.load(params);
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