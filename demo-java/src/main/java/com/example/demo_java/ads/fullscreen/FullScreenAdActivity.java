package com.example.demo_java.ads.fullscreen;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demo_java.R;
import com.example.demo_java.misc.Constant;

public abstract class FullScreenAdActivity extends AppCompatActivity {
    protected String adUnitName;
    protected TextView statusTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        adUnitName = getIntent().getStringExtra(Constant.adUnitNameKey);
        setTitle(adUnitName);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        statusTextView = findViewById(R.id.status_text_view);

        loadAd();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract void loadAd();
    protected abstract void showAd();
}