package com.example.demo_java.ads.view.nativead;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.demo_java.R;
import com.example.demo_java.misc.Constant;
import com.intergi.playwiresdk.ads.view.PWViewAd;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeView;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewFactory;

public class NativeAdActivity extends AppCompatActivity {
    private String adUnitName;
    private ConstraintLayout constraintLayout;
    private TextView statusTextView;
    private PWNativeView nativeAd;
    private boolean isNativeAdded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);

        adUnitName = getIntent().getStringExtra(Constant.adUnitNameKey);
        if (adUnitName == null) {
            adUnitName = "";
        }
        setTitle(adUnitName);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        constraintLayout = findViewById(R.id.container);
        statusTextView = findViewById(R.id.status_text_view);

        loadNativeAd();
    }

    @Override
    protected void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }

    private void loadNativeAd() {
        PWViewAd.Listener listener = new PWViewAd.Listener() {
            @Override
            public void onViewAdImpression(@NonNull PWViewAd pwViewAd) {

            }

            @Override
            public void onViewAdClicked(@NonNull PWViewAd pwViewAd) {

            }

            @Override
            public void onViewAdClosed(@NonNull PWViewAd pwViewAd) {
                statusTextView.setText(getString(R.string.native_ad_shown, adUnitName));
            }

            @Override
            public void onViewAdOpened(@NonNull PWViewAd pwViewAd) {

            }

            @Override
            public void onViewAdLoaded(@NonNull PWViewAd ad) {
                addNativeAd();
                statusTextView.setText(getString(R.string.native_ad_loaded, adUnitName));
            }

            @Override
            public void onViewAdFailedToLoad(@NonNull PWViewAd ad) {
                statusTextView.setText(getString(R.string.native_ad_load_failed, adUnitName));
            }
        };

        PWNativeViewFactory factory = new PWNativeViewFactory() {
            @NonNull
            @Override
            public View createAdContentView(@NonNull PWNativeView nativeView, @NonNull PWNativeViewContent adContent) {
                View adView = LayoutInflater.from(NativeAdActivity.this).inflate(R.layout.view_native_ad, null);
                if (adView instanceof NativeView) {
                    ((NativeView) adView).configure(adContent);
                }
                return adView;
            }

            @Nullable
            @Override
            public View callToActionView(@NonNull PWNativeView nativeView, @NonNull View adContentView) {
                if (adContentView instanceof NativeView) {
                    return ((NativeView) adContentView).getActionButton();
                }
                return null;
            }
        };

        nativeAd = new PWNativeView(this, adUnitName, factory, listener);
        nativeAd.load();

        statusTextView.setText(getString(R.string.native_ad_loading, adUnitName));
    }

    private void addNativeAd() {
        if (isNativeAdded) {
            return;
        }
        isNativeAdded = true;

        nativeAd.setId(View.generateViewId());

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;

        constraintLayout.addView(nativeAd, layoutParams);

        ConstraintLayout.LayoutParams statusParams = (ConstraintLayout.LayoutParams) statusTextView.getLayoutParams();
        statusParams.bottomToTop = nativeAd.getId();
        statusTextView.setLayoutParams(statusParams);
    }
}
