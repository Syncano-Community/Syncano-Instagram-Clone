package com.solid9studio.instagram.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.solid9studio.instagram.constant.Constants;
import com.syncano.library.SyncanoBuilder;
import com.syncano.library.data.User;

/**
 * Created by sebastian on 2016-11-23.
 */

public class Instagram extends Application {

    public static final String USER_KEY = "user_key";

    private User user;

    @Override
    public void onCreate()
    {
        super.onCreate();
        new SyncanoBuilder().apiKey(Constants.API_KEY).instanceName(Constants.INSTANCE_NAME).androidContext(this).setAsGlobalInstance(true).build();

        setUser(new User());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserKey() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        if(settings.contains(USER_KEY))
        {
            return settings.getString(USER_KEY, "");
        }

        return "";
    }

    public void setUserKey(String userKey) {
        if(userKey != null && !userKey.isEmpty()) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(USER_KEY, userKey);
            editor.commit();
        }
    }
}
