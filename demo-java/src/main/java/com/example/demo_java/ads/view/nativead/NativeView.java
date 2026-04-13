package com.example.demo_java.ads.view.nativead;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.demo_java.R;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContent;
import com.intergi.playwiresdk.ads.view.nativead.PWNativeViewContentView;

public class NativeView extends ConstraintLayout implements PWNativeViewContentView {

    public NativeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public NativeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public TextView getHeadlineTextView() {
        return findViewById(R.id.ad_headline);
    }

    @Override
    public TextView getAdAttributionView() {
        return findViewById(R.id.ad_attribution);
    }

    @Override
    public ViewGroup getMediaView() {
        return findViewById(R.id.ad_media);
    }

    @Override
    public Button getCallToActionButton() {
        return findViewById(R.id.ad_call_to_action);
    }

    @Override
    public TextView getBodyTextView() {
        return findViewById(R.id.ad_body);
    }

    @Override
    public ImageView getIconImageView() {
        return findViewById(R.id.ad_app_icon);
    }

    @Override
    public TextView getAdvertiserTextView() {
        return findViewById(R.id.ad_advertiser);
    }

    @Override
    public TextView getStoreTextView() {
        return findViewById(R.id.ad_store);
    }

    @Override
    public TextView getPriceTextView() {
        return findViewById(R.id.ad_price);
    }

    @Override
    public ViewGroup getStarRatingView() {
        return null;
    }

    @Override
    public void didSetAdContent(PWNativeViewContent adContent) {
        ViewGroup.LayoutParams params = getMediaView().getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getMediaView().setLayoutParams(params);
    }
}