package com.solid9studio.instagram.screen.splashScreen;

import android.os.Bundle;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.screen.loginScreen.LoginActivity;
import com.solid9studio.instagram.screen.postListScreen.PostListActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isLoggedIn()) {
            startActivity(PostListActivity.getActivityIntent(this));
        } else {
            startActivity(LoginActivity.getActivityIntent(this));
        }
    }

    private boolean isLoggedIn() {
        if(((Instagram) this.getApplication()).getUser() != null)
        {
            return true;
        }
        return false;
    }
}
