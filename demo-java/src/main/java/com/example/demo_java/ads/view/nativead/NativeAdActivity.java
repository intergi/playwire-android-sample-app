package com.example.demo_java.ads.view.nativead;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demo_java.R;
import com.example.demo_java.misc.Constant;
import com.intergi.playwiresdk.ads.PWAdError;
import com.intergi.playwiresdk.ads.view.PWViewAd;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeView;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContentView;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewFactory;

public class NativeAdActivity extends AppCompatActivity {
    private String adUnitName;
    private FrameLayout nativeAdContainer;
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

        nativeAdContainer = findViewById(R.id.native_ad_container);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNativeAd() {
        PWViewAd.Listener listener = new PWViewAd.Listener() {
            @Override
            public void onViewAdLoaded(@NonNull PWViewAd ad) {
                addNativeAd();
                nativeAd.setVisibility(View.VISIBLE);
                nativeAdContainer.setVisibility(View.VISIBLE);
                statusTextView.setText(getString(R.string.native_ad_loaded, adUnitName));
            }

            @Override
            public void onViewAdFailedToLoad(@NonNull PWViewAd ad, @NonNull PWAdError error) {
                if (nativeAd != null) {
                    nativeAd.setVisibility(View.GONE);
                }
                nativeAdContainer.setVisibility(View.GONE);
                statusTextView.setText(getString(R.string.native_ad_load_failed, adUnitName));
            }

            @Override
            public void onViewAdClosed(@NonNull PWViewAd ad) {
                statusTextView.setText(getString(R.string.native_ad_shown, adUnitName));
            }

            @Override
            public void onViewAdOpened(@NonNull PWViewAd ad) {
                statusTextView.setText("Native ad opened: " + adUnitName);
            }

            @Override
            public void onViewAdImpression(@NonNull PWViewAd ad) {
                statusTextView.setText("Native ad impression recorded: " + adUnitName);
            }

            @Override
            public void onViewAdClicked(@NonNull PWViewAd ad) {
                statusTextView.setText("Native ad clicked: " + adUnitName);
            }
        };

        PWNativeViewFactory factory = new PWNativeViewFactory() {
            @NonNull
            @Override
            public PWNativeViewContentView createAdContentView() {
                return (PWNativeViewContentView) getLayoutInflater().inflate(
                        R.layout.view_native_ad,
                        null
                );
            }
        };

        nativeAd = new PWNativeView(this, adUnitName, factory, listener);
        nativeAd.setVisibility(View.GONE);
        nativeAdContainer.setVisibility(View.GONE);

        nativeAd.load();

        statusTextView.setText(getString(R.string.native_ad_loading, adUnitName));
    }

    private void addNativeAd() {
        if (isNativeAdded) {
            return;
        }
        isNativeAdded = true;

        nativeAdContainer.addView(
                nativeAd,
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );
    }
}