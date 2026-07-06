package com.example.demo_java.ads.view.banner;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.demo_java.R;
import com.example.demo_java.misc.Constant;
import com.intergi.playwiresdk.ads.PWAdError;
import com.intergi.playwiresdk.ads.view.PWViewAd;
import com.intergi.playwiresdk.ads.view.banner.PWBannerView;

public class BannerActivity extends AppCompatActivity {
    private String adUnitName;
    private ConstraintLayout constraintLayout;
    private TextView statusTextView;
    private PWBannerView banner;
    private boolean isBannerAdded = false;

    @Override
    protected void onDestroy() {
        if (banner != null) {
            banner.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        constraintLayout = findViewById(R.id.container);
        statusTextView = findViewById(R.id.status_text_view);

        adUnitName = getIntent().getStringExtra(Constant.adUnitNameKey);
        if (adUnitName == null) {
            adUnitName = "";
        }
        setTitle(adUnitName);

        loadBanner();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadBanner() {
        PWViewAd.Listener listener = new PWViewAd.Listener() {
            @Override
            public void onViewAdImpression(@NonNull PWViewAd pwViewAd) {}

            @Override
            public void onViewAdClicked(@NonNull PWViewAd pwViewAd) {}

            @Override
            public void onViewAdClosed(@NonNull PWViewAd pwViewAd) {}

            @Override
            public void onViewAdOpened(@NonNull PWViewAd pwViewAd) {}

            @Override
            public void onViewAdLoaded(@NonNull PWViewAd ad) {
                addBannerToParent();
                banner.setVisibility(View.VISIBLE);
                statusTextView.setText(getString(R.string.banner_ad_loaded, adUnitName));
            }

            @Override
            public void onViewAdFailedToLoad(@NonNull PWViewAd ad, @NonNull PWAdError error) {
                if (banner != null) {
                    banner.setVisibility(View.GONE);
                }
                statusTextView.setText(getString(R.string.banner_ad_load_failed, adUnitName));
            }
        };

        banner = new PWBannerView(this, adUnitName, listener);

        banner.setVisibility(View.GONE);

        banner.load();

        statusTextView.setText(getString(R.string.banner_ad_loading, adUnitName));
    }

    private void addBannerToParent() {
        if (isBannerAdded) {
            return;
        }
        isBannerAdded = true;

        banner.setId(View.generateViewId());
        constraintLayout.addView(banner);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(banner.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.connect(banner.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(banner.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.constrainWidth(banner.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(banner.getId(), ConstraintSet.WRAP_CONTENT);

        constraintSet.applyTo(constraintLayout);
    }
}