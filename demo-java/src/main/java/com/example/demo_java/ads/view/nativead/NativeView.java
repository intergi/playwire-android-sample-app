package com.example.demo_java.ads.view.nativead;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.demo_java.R;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent;

public class NativeView extends LinearLayout {
    private TextView headlineTextView;
    private TextView bodyTextView;
    private Button actionButton;
    private ImageView appIconImageView;
    private TextView storeTextView;
    private TextView priceTextView;
    private TextView advertiserTextView;
    private FrameLayout mediaViewHolder;

    public NativeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NativeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Button getActionButton() {
        return actionButton;
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

    public void configure(PWNativeViewContent adContent) {
        headlineTextView.setVisibility(adContent.getHeadline() != null ? VISIBLE : INVISIBLE);
        headlineTextView.setText(adContent.getHeadline());

        bodyTextView.setVisibility(adContent.getBody() != null ? VISIBLE : INVISIBLE);
        bodyTextView.setText(adContent.getBody());

        actionButton.setVisibility(adContent.getCallToAction() != null ? VISIBLE : INVISIBLE);
        actionButton.setText(adContent.getCallToAction());

        priceTextView.setVisibility(adContent.getPrice() != null ? VISIBLE : INVISIBLE);
        priceTextView.setText(adContent.getPrice());

        storeTextView.setVisibility(adContent.getStore() != null ? VISIBLE : INVISIBLE);
        storeTextView.setText(adContent.getStore());

        advertiserTextView.setVisibility(adContent.getAdvertiser() != null ? VISIBLE : INVISIBLE);
        advertiserTextView.setText(adContent.getAdvertiser());

        appIconImageView.setVisibility(adContent.getIcon() != null ? VISIBLE : INVISIBLE);
        appIconImageView.setImageDrawable(adContent.getIcon());

        mediaViewHolder.setVisibility(adContent.getMediaView() != null ? VISIBLE : INVISIBLE);
        if (adContent.getMediaView() != null) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.weight = 1.0f;
            adContent.getMediaView().setLayoutParams(params);
            mediaViewHolder.addView(adContent.getMediaView());
        }
    }
}
