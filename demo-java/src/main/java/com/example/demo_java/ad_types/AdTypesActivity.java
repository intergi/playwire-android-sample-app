package com.example.demo_java.ad_types;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo_java.BuildConfig;
import com.example.demo_java.R;
import com.example.demo_java.ads.fullscreen.app_open.AppOpenAdActivity;
import com.example.demo_java.ads.fullscreen.interstitial.InterstitialActivity;
import com.example.demo_java.ads.fullscreen.rewarded.RewardedActivity;
import com.example.demo_java.ads.view.banner.BannerActivity;
import com.example.demo_java.ads.view.nativead.NativeAdActivity;
import com.example.demo_java.misc.Constant;
import com.intergi.playwiresdk.PWAdMode;
import com.intergi.playwiresdk.PWConfig;
import com.intergi.playwiresdk.PWNotifier;
import com.intergi.playwiresdk.PlaywireSDK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdTypesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_types);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (BuildConfig.DEBUG) {
            PWNotifier.INSTANCE.startConsoleLogger();
            PlaywireSDK.INSTANCE.setTest(true);
        }

        PlaywireSDK.INSTANCE.initialize("1024407", "703", this, () -> {
            setupRecyclerView();
            return null;
        });
    }

    private void setupRecyclerView() {
        PWConfig config = PlaywireSDK.INSTANCE.getConfig();

        List<Pair<PWAdMode, String>> adUnits;
        if (config != null && config.getAdUnits() != null) {
            adUnits = Arrays.stream(config.getAdUnits())
                    .map(adUnit -> new Pair<>(adUnit.getMode(), adUnit.getName()))
                    .collect(Collectors.toList());
        } else {
            adUnits = new ArrayList<>();
        }

        RecyclerView recyclerView = findViewById(R.id.ad_units_recycler_view);
        AdTypesAdapter adapter = new AdTypesAdapter(adUnit -> {
            String adUnitName = adUnit.second;
            PWAdMode adMode = adUnit.first;
            showAdUnitActivity(adUnitName, adMode);
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.submitList(adUnits);
    }

    private void showAdUnitActivity(String adUnitName, PWAdMode mode) {
        Class<?> activityClass;
        switch (mode) {
            case Banner:
                activityClass = BannerActivity.class;
                break;
            case Interstitial:
                activityClass = InterstitialActivity.class;
                break;
            case Rewarded:
                activityClass = RewardedActivity.class;
                break;
            case AppOpenAd:
                activityClass = AppOpenAdActivity.class;
                break;
            case Native:
                activityClass = NativeAdActivity.class;
                break;
            default:
                throw new IllegalArgumentException("Invalid ad mode: " + mode);
        }

        Intent intent = new Intent(this, activityClass);
        intent.putExtra(Constant.adUnitNameKey, adUnitName);
        startActivity(intent);
    }
}
