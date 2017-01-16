package com.solid9studio.instagram.application;

import android.app.Application;

import com.solid9studio.instagram.constant.Constants;
import com.solid9studio.instagram.user.InstagramUser;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoBuilder;

/**
 * Extend application class, so Syncano object can be accessed globally.
 */

public class Instagram extends Application {

    private Syncano syncano;

    @Override
    public void onCreate()
    {
        super.onCreate();
        syncano = new SyncanoBuilder().apiKey(Constants.API_KEY)
                .instanceName(Constants.INSTANCE_NAME)
                .androidContext(this)
                .setAsGlobalInstance(true)
                .useLoggedUserStorage(true)
                .build();
    }

    public InstagramUser getUser() {
        return (InstagramUser)syncano.getUser();
    }

    public Syncano getSyncanoInstance() {
        return syncano;
    }
}
