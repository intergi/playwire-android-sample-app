package com.playwire.demo_java.ads.view.nativead;

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
import com.intergi.playwiresdk.ads.view.PWViewAd;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeView;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewFactory;
import com.playwire.demo_java.R;
import com.playwire.demo_java.misc.Constant;

public class NativeAdActivity extends AppCompatActivity {

    PWNativeView nativeView;
    /// The ad unit name, e.g. 'banner-320x50', 'interstitial-home', 'rewarded-coins', etc.
    String adUnitName;
    Boolean isNativeAdded = false;
    TextView statusTextView;
    Button refreshButton;

    @Override
    protected void onDestroy() {
        /// Must call the `destroy` method to avoid memory leak.
        if (nativeView != null) {
            nativeView.destroy();
        }

        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_native_ad);

        adUnitName = getIntent().getStringExtra(Constant.adUnitNameKey);
        statusTextView = findViewById(R.id.status_text_view);
        refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        loadNativeAd();
    }

    void loadNativeAd() {
        PWViewAd.Listener listener = new PWViewAd.Listener() {
            @Override
            public void onViewAdLoaded(@NonNull PWViewAd pwViewAd) {
                statusTextView.setText(String.format("✅ The native ad \"%s\" is loaded.", adUnitName));

                addNativeAd();
            }

            @Override
            public void onViewAdFailedToLoad(@NonNull PWViewAd pwViewAd) {
                statusTextView.setText(String.format("❌ Failed to load the native ad \"%s\".", adUnitName));
            }

            @Override
            public void onViewAdOpened(@NonNull PWViewAd pwViewAd) {
            }

            @Override
            public void onViewAdClosed(@NonNull PWViewAd pwViewAd) {
                statusTextView.setText(String.format("👍 The native ad content \"%s\" was successfully shown.", adUnitName));
            }

            @Override
            public void onViewAdClicked(@NonNull PWViewAd pwViewAd) {
            }

            @Override
            public void onViewAdImpression(@NonNull PWViewAd pwViewAd) {
            }
        };


        PWNativeViewFactory factory = new PWNativeViewFactory() {
            @NonNull
            @Override
            public View createAdContentView(@NonNull PWNativeView pwNativeView, @NonNull PWNativeViewContent pwNativeViewContent) {
                // Inflates your custom view which can be configurable with `PWNativeViewContent`.
                // `NativeView` is a `ViewGroup` subclass for our custom native ad layout. See `NativeView` class for more details.
                View adView = getLayoutInflater().inflate(R.layout.view_native_ad, null);
                if(!adView.getClass().equals(NativeView.class)) return adView;
                ((NativeView) adView).configure(pwNativeViewContent);
                return adView;
            }

            @Nullable
            @Override
            public View callToActionView(@NonNull PWNativeView pwNativeView, @NonNull View view) {
                if(!view.getClass().equals(NativeView.class)) return null;
                return ((NativeView) view).actionButton;
            }
        };
        
        nativeView = new PWNativeView(getApplicationContext(), adUnitName, factory, listener);

        /// Use `new PWLoadParams().withTargeting()` to pass your custom targets to ad request.
        /// PWLoadParams params = new PWLoadParams().withTargeting(new HashMap<String, String>() {{
        ///   put("age", "18-35");
        ///   put("page", "travel");
        /// }});
        /// nativeView.load(params);

        nativeView.load();
        statusTextView.setText(String.format("⏳ The native ad \"%s\" is loading.", adUnitName));
    }

    private void refresh() {
        /// Refresh will start only if the ad unit contains `refresh` object.
        /// See logs from `PWNotifier` to track status of refresh.
        if (nativeView == null) return;
        nativeView.refresh();

        PWAdUnit adUnit = null;
        for (PWAdUnit item: PlaywireSDK.INSTANCE.getConfig().getAdUnits()) {
            String name = item.getName();
            if (name != null && name.equals(adUnitName)) {
                adUnit = item;
                break;
            }
        }
        if (adUnit.getRefresh() == null) {
            statusTextView.setText(String.format("⚠️ The native ad \"%s\" can't be refreshed manually.\nSee logs to get more details.", adUnitName));
            return;
        }
        statusTextView.setText(String.format("🔄 The native ad \"%s\" is refreshing.", adUnitName));
    }

    private void addNativeAd() {
        if (isNativeAdded || nativeView == null) return;
        isNativeAdded = true;

        /// Native view is ready to be added to view hierarchy.
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);
        RelativeLayout container = findViewById(R.id.native_ad_container);
        container.addView(nativeView, layoutParams);
    }
}