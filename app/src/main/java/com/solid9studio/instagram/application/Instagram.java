package com.solid9studio.instagram.application;

import android.app.Application;
import android.os.Trace;
import android.preference.PreferenceManager;

import com.google.gson.JsonObject;
import com.solid9studio.instagram.constant.Constants;
import com.solid9studio.instagram.push.GetApplicationTokenTask;
import com.solid9studio.instagram.user.InstagramUser;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoBuilder;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.PushDevice;

/**
 * Created by sebastian on 2016-11-23.
 */

public class Instagram extends Application {

    private Syncano syncano;

    @Override
    public void onCreate()
    {
        super.onCreate();
        syncano = new SyncanoBuilder().apiKey(Constants.API_KEY).instanceName(Constants.INSTANCE_NAME).androidContext(this).setAsGlobalInstance(true).useLoggedUserStorage(true).build();
    }

    public InstagramUser getUser() {
        return (InstagramUser)syncano.getUser();
    }

    public Syncano getSyncanoInstance() {
        return syncano;
    }
}
