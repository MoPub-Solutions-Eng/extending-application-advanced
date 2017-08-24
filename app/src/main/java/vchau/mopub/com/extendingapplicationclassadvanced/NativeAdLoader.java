package vchau.mopub.com.extendingapplicationclassadvanced;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;

public class NativeAdLoader extends Application {

    final String TAG = this.getClass().getName();
    public Context mContext;
    public RelativeLayout parentView;

    public OnAdLoadedListener onAdLoadedListener;

    public interface OnAdLoadedListener {
        void onAdLoaded();
    }

    // Custom callback to be forwarded to MainActivity, letting publishers know the ad has finished loading.
    public void setOnAdLoadedListener(OnAdLoadedListener eventListener) {
        this.onAdLoadedListener = eventListener;
    }

    public NativeAdLoader(Context applicationContext) {
        mContext = applicationContext;
    }

    public void initAndRequest(String adUnitId) {

        AdapterHelper adapterHelper = new AdapterHelper(mContext, 0, 2);

        ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_layout)
                .mainImageId(R.id.native_main_image)
                .iconImageId(R.id.native_icon_image)
                .titleId(R.id.native_title)
                .textId(R.id.native_text)
                .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                .build();

        MoPubStaticNativeAdRenderer moPubAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);

        makeAdRequest(adapterHelper, moPubAdRenderer, adUnitId);
    }

    public void makeAdRequest(final AdapterHelper adapterHelper, MoPubStaticNativeAdRenderer moPubAdRenderer, String adUnitId) {

        final NativeAd.MoPubNativeEventListener moPubNativeEventListener = new NativeAd.MoPubNativeEventListener() {
            @Override
            public void onImpression(View view) {
                Log.d(TAG, "onImpression");
            }

            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick");
            }
        };

        MoPubNative.MoPubNativeNetworkListener moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {
            @Override
            public void onNativeLoad(NativeAd nativeAd) {
                Log.d(TAG, "onNativeLoad");

                parentView = new RelativeLayout(mContext);
                parentView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                RelativeLayout nativeAdView = new RelativeLayout(mContext);
                nativeAdView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

                nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);

                View v = adapterHelper.getAdView(null, nativeAdView, nativeAd, new ViewBinder.Builder(0).build());
                v.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                parentView.addView(v);

                onAdLoadedListener.onAdLoaded();
            }

            @Override
            public void onNativeFail(NativeErrorCode errorCode) {
                Log.d(TAG, "onNativeFail: " + errorCode.toString());
            }
        };

        MoPubNative moPubNative = new MoPubNative(mContext, adUnitId, moPubNativeNetworkListener);

        moPubNative.registerAdRenderer(moPubAdRenderer);
        moPubNative.makeRequest();
    }

    public RelativeLayout getAdView() {
        return (parentView != null) ? (parentView) : (new RelativeLayout(mContext));
    }
}