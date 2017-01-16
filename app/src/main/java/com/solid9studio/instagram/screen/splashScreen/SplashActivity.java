package com.solid9studio.instagram.screen.splashScreen;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.GoogleApiAvailability;
import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.screen.loginScreen.LoginActivity;
import com.solid9studio.instagram.screen.postListScreen.PostListActivity;

import com.google.android.gms.common.ConnectionResult;
import com.solid9studio.instagram.utilities.Utilities;

public class SplashActivity extends BaseActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Instagram instagram = (Instagram) this.getApplication();

        if (!checkPlayServices()) {
            Utilities.showToast(getApplicationContext(), "Your device does not have Google Play Services Installed! It can't receive PUSH notifications!!");
        }

        // Delay so splash will be visible.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if (isLoggedIn(instagram)) {
                    startActivity(PostListActivity.getActivityIntent(SplashActivity.this));
                } else {
                    startActivity(LoginActivity.getActivityIntent(SplashActivity.this));
                }
            }
        }, 2000);
    }

    private boolean isLoggedIn(Instagram instagram) {
        if(instagram.getUser() != null)
        {
            return true;
        }
        return false;
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
