package com.solid9studio.instagram.screen.createPostScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;

public class CreatePostActivity extends BaseActivity {

    public static Intent getActivityIntent(Context context) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                sharePost();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sharePost() {
        // Share post if is valid.
    }
}
