package com.playwire.demo_java.ads.view.nativead;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent;
import com.playwire.demo_java.R;

public class NativeView extends LinearLayout {

    TextView headlineTextView;
    TextView bodyTextView;
    ImageView appIconImageView;
    TextView storeTextView;
    TextView priceTextView;
    TextView advertiserTextView;
    Button actionButton;
    FrameLayout mediaViewHolder;

    public NativeView(Context context) {
        super(context);
    }

    public NativeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NativeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NativeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        appIconImageView = findViewById(R.id.ad_app_icon);
        headlineTextView = findViewById(R.id.ad_headline);
        bodyTextView = findViewById(R.id.ad_body);
        actionButton = findViewById(R.id.ad_call_to_action);
        storeTextView = findViewById(R.id.ad_store);
        priceTextView = findViewById(R.id.ad_price);
        advertiserTextView = findViewById(R.id.ad_advertiser);
        mediaViewHolder = findViewById(R.id.ad_media);
    }
    
    void configure(PWNativeViewContent adContent) {
        /// Configure views with ad content.
        headlineTextView.setVisibility(adContent.getHeadline() != null ? View.VISIBLE : View.GONE);
        headlineTextView.setText(adContent.getHeadline());

        /// Hide view in case ad content doesn't contain required information.
        bodyTextView.setVisibility(adContent.getBody() != null ? View.VISIBLE : View.GONE);
        bodyTextView.setText(adContent.getBody());

        actionButton.setVisibility(adContent.getCallToAction() != null ? View.VISIBLE : View.GONE);
        actionButton.setText(adContent.getCallToAction());

        priceTextView.setVisibility(adContent.getPrice() != null ? View.VISIBLE : View.GONE);
        priceTextView.setText(adContent.getPrice());

        storeTextView.setVisibility(adContent.getStore() != null ? View.VISIBLE : View.GONE);
        storeTextView.setText(adContent.getStore());

        advertiserTextView.setVisibility(adContent.getAdvertiser() != null ? View.VISIBLE : View.GONE);
        advertiserTextView.setText(adContent.getAdvertiser());

        appIconImageView.setVisibility(adContent.getIcon() != null ? View.VISIBLE : View.GONE);
        appIconImageView.setImageDrawable(adContent.getIcon());

        mediaViewHolder.setVisibility(adContent.getMediaView() != null ? View.VISIBLE : View.GONE);
        if (adContent.getMediaView() != null) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.weight = 1.0f;
            adContent.getMediaView().setLayoutParams(params);
            mediaViewHolder.addView(adContent.getMediaView());
        }
    }
}