package com.solid9studio.instagram.screen.profileScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;

public class ProfileActivity extends BaseActivity {

    public static Intent getActivityIntent(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
