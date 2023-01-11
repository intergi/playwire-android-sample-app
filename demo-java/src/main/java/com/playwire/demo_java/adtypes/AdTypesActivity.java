package com.playwire.demo_java.adtypes;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.intergi.playwiresdk.PWAdMode;
import com.intergi.playwiresdk.PWNotifier;
import com.intergi.playwiresdk.PlaywireSDK;
import com.playwire.demo_java.BuildConfig;
import com.playwire.demo_java.R;
import com.playwire.demo_java.ads.fullscreen.appopenad.AppOpenAdActivity;
import com.playwire.demo_java.ads.fullscreen.interstitial.InterstitialActivity;
import com.playwire.demo_java.ads.fullscreen.rewarded.RewardedActivity;
import com.playwire.demo_java.ads.fullscreen.rewardedinterstitial.RewardedInterstitialActivity;
import com.playwire.demo_java.ads.view.banner.AnchoredBannerActivity;
import com.playwire.demo_java.ads.view.banner.BannerActivity;
import com.playwire.demo_java.ads.view.banner.InlineBannerActivity;
import com.playwire.demo_java.ads.view.nativead.NativeAdActivity;
import com.playwire.demo_java.misc.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kotlin.Pair;

public class AdTypesActivity extends AppCompatActivity {

    TextView statusTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_types);

        statusTextView = findViewById(R.id.status_text_view);
        statusTextView.setText("⏳ SDK initializaton..");
        if (BuildConfig.DEBUG) {
            /// Start `PWNotifier` to log SDK events to console.
            PWNotifier.INSTANCE.startConsoleLogger();
        }

        /// Initialize Playwire SDK with `publisherId` and `appId`, when initialization done, you will be able to load ad units.
        /// Make sure you run SDK initialization only once.
        PlaywireSDK.INSTANCE.initialize ("playwire", "test", this, () -> {
            statusTextView.setText(null);
            setupListView();
            return null;
        });
    }

    private void setupListView() {
        List<Pair<PWAdMode, String>> adUnits = PlaywireSDK.INSTANCE.adUnitNames();
        Collections.sort(adUnits, new Comparator<Pair<PWAdMode, String>>() {
            @Override
            public int compare(Pair<PWAdMode, String> obj1, Pair<PWAdMode, String> obj2) {
                return obj1.component2().compareTo(obj2.component2());
            }
        });
        List<String> names = new ArrayList();
        for (Pair<PWAdMode, String> adUnit: adUnits) {
            names.add(adUnit.getSecond());
        }

        ArrayAdapter adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                names.toArray()
        );

        ListView listView = findViewById(R.id.ad_units_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pair<PWAdMode, String> adUnit = adUnits.get(i);
                showAdUnitActivity(adUnit.getSecond(), adUnit.getFirst());
            }
        });
    }

    private void showAdUnitActivity(String adUnitName, PWAdMode mode) {
        Class activityClass;
        switch (mode.name()) {
            case "Banner":
                activityClass = BannerActivity.class;
                break;
            case "BannerInline":
                activityClass = InlineBannerActivity.class;
                break;
            case "BannerAnchored":
                activityClass = AnchoredBannerActivity.class;
                break;
            case "Interstitial":
                activityClass = InterstitialActivity.class;
                break;
            case "Rewarded":
                activityClass = RewardedActivity.class;
                break;
            case "AppOpenAd":
                activityClass = AppOpenAdActivity.class;
                break;
            case "RewardedInterstitial":
                activityClass = RewardedInterstitialActivity.class;
                break;
            case "Native":
                activityClass = NativeAdActivity.class;
                break;
            default:
                System.exit(0);
                return;
        };

        Intent intent = new Intent(AdTypesActivity.this, activityClass);
        intent.putExtra(Constant.adUnitNameKey, adUnitName);
        startActivity(intent);
    }
}
