package com.solid9studio.instagram.push;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.solid9studio.instagram.constant.Constants;
import com.solid9studio.instagram.user.InstagramUser;

import com.syncano.library.Syncano;
import com.syncano.library.api.Response;
import com.syncano.library.data.PushDevice;

import com.google.android.gms. gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * This class is used to obtain token for PUSH messages.
 */

public class GetApplicationTokenTask extends AsyncTask<Void, Void, String> {

    private static final String TAG = MyGcmListenerService.class.getSimpleName();

    private Context ctx;
    private InstagramUser profile;

    public GetApplicationTokenTask(Context ctx, InstagramUser profile) {
        this.ctx = ctx;
        this.profile = profile;
    }

    @Override
    protected String doInBackground(Void... params) {
        // get gcm token from Google
        String token = getGCMTokenFromGoogle();
        if (token == null || token.isEmpty()) {
            Log.w(TAG, "Error getting GCM token from Google");
            return "";
        }
        Log.i(TAG, "GCM Registration Token: " + token);

        // send gcm token to Syncano
        Response syncanoResponse = Syncano.getInstance().registerPushDevice(new PushDevice(token)).send();
        if (!syncanoResponse.isSuccess()) {
            Log.e(TAG, "Error sending GCM token to Syncano: " + syncanoResponse.getHttpResultCode() + " " + syncanoResponse.getError());
            return "";
        }

        // save token in preferences
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(Constants.TOKEN, token).apply();

        profile.getProfile().setPushUrl(token);
        profile.getProfile().save();

        return token;
    }

    private String getGCMTokenFromGoogle() {
        try {
            InstanceID instanceID = InstanceID.getInstance(ctx);
            return instanceID.getToken(Constants.GCM_DEFAULT_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
