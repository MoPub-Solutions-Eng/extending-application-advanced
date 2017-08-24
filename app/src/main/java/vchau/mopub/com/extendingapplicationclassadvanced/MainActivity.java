package vchau.mopub.com.extendingapplicationclassadvanced;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button initBtn = (Button) findViewById(R.id.initBtn);
        final LinearLayout rootLayout = (LinearLayout) findViewById(R.id.rootLayout);

        initBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String adUnitId = "11a17b188668469fb0412708c3d16813";

                final NativeAdLoader nativeAdLoader = new NativeAdLoader(getApplicationContext());
                nativeAdLoader.initAndRequest(adUnitId);

                // NativeAdLoader's (not MoPub SDK's) custom ad listener

                nativeAdLoader.setOnAdLoadedListener(new NativeAdLoader.OnAdLoadedListener() {
                    @Override
                    public void onAdLoaded() {
                        RelativeLayout mNativeAdView = nativeAdLoader.getAdView();

                        mNativeAdView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                        rootLayout.addView(mNativeAdView);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*
        Call clean-up code to properly dispose of your ad instances.
         */
    }
}
