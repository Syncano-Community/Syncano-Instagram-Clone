package com.solid9studio.instagram.push;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.solid9studio.instagram.R;
import com.solid9studio.instagram.constant.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.api.Response;
import com.syncano.library.data.PushDevice;

import com.google.android.gms. gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Sebastian on 2017-01-12.
 */

public class GetApplicationTokenTask extends AsyncTask<Void, Void, String> {
    private Context ctx;

    public GetApplicationTokenTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(Void... params) {
        // get gcm token from Google
        String token = getGCMTokenFromGoogle();
        if (token == null || token.isEmpty()) {
            Log.w("ASD", "Error getting GCM token from Google");
            return "";
        }
        Log.i("ASD", "GCM Registration Token: " + token);

        // send gcm token to Syncano
        Response syncanoResponse = Syncano.getInstance().registerPushDevice(new PushDevice(token)).send();
        if (!syncanoResponse.isSuccess()) {
            Log.e("ASD", "Error sending GCM token to Syncano: " + syncanoResponse.getHttpResultCode() + " " + syncanoResponse.getError());
            return "";
        }

        // save token
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString(Constants.TOKEN, token).apply();

        return token;
    }

    private String getGCMTokenFromGoogle() {
        try {
            InstanceID instanceID = InstanceID.getInstance(ctx);
            return instanceID.getToken(ctx.getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}