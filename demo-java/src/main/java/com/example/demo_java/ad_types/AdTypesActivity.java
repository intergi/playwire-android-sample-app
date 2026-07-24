package com.example.demo_java.ad_types;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo_java.R;
import com.example.demo_java.ads.fullscreen.app_open.AppOpenAdActivity;
import com.example.demo_java.ads.fullscreen.interstitial.InterstitialActivity;
import com.example.demo_java.ads.fullscreen.rewarded.RewardedActivity;
import com.example.demo_java.ads.view.banner.BannerActivity;
import com.example.demo_java.ads.view.nativead.NativeAdActivity;
import com.example.demo_java.misc.Constant;
import com.intergi.playwiresdk.PlaywireSDK;
import com.intergi.playwiresdk.logger.LogLevel;

import java.util.List;

public class AdTypesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_types);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PlaywireSDK.INSTANCE.setLogLevel(LogLevel.INFO);
        PlaywireSDK.INSTANCE.setTest(false);

        PlaywireSDK.INSTANCE.start("1024407", "703", this, (success, error) -> {
            if (success) {
                setupRecyclerView();
            } else {
                Toast.makeText(
                        this,
                        error != null && error.getMessage() != null
                                ? error.getMessage()
                                : "Playwire SDK initialization failed.",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.ad_units_recycler_view);

        AdTypesAdapter adapter = new AdTypesAdapter(item -> {
            String adUnitName = item.first;
            Class<? extends Activity> activityClass = item.second;
            Intent intent = new Intent(this, item.second);
            intent.putExtra(Constant.adUnitNameKey, adUnitName);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Pair<String, Class<? extends Activity>>> adUnits = List.of(
                new Pair("banner-320x50-gam", BannerActivity.class),
                new Pair("banner-320x50-max", BannerActivity.class),
                new Pair("banner-300x250-gam", BannerActivity.class),
                new Pair("banner-300x250-max", BannerActivity.class),
                new Pair("native-gam", NativeAdActivity.class),
                new Pair("native-max", NativeAdActivity.class),
                new Pair("app-open-gam", AppOpenAdActivity.class),
                new Pair("app-open-max", AppOpenAdActivity.class),
                new Pair("interstitial-gam", InterstitialActivity.class),
                new Pair("interstitial-max", InterstitialActivity.class),
                new Pair("rewarded-gam", RewardedActivity.class),
                new Pair("rewarded-video-max", RewardedActivity.class),
                new Pair("floating-banner", BannerActivity.class)
        );
        adapter.submitList(adUnits);
    }
}