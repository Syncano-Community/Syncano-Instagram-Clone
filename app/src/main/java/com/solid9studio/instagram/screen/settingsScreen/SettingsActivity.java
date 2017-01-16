package com.solid9studio.instagram.screen.settingsScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.constant.Constants;
import com.solid9studio.instagram.screen.loginScreen.LoginActivity;
import com.solid9studio.instagram.screen.postListScreen.PostListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.switch_notifications)
    Switch notificationsSwitch;

    public static Intent getActivityIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        SharedPreferences sharedPref = getSharedPreferences(Constants.USE_PUSH, Context.MODE_PRIVATE);
        notificationsSwitch.setChecked(sharedPref.getBoolean(Constants.USE_PUSH, true));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveChanges();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveChanges() {

        SharedPreferences sharedPref = getSharedPreferences(
               Constants.USE_PUSH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.USE_PUSH, notificationsSwitch.isChecked());
        editor.commit();

        Intent intent = new Intent(this, PostListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.sign_out_button)
    public void signOut() {
        ((Instagram) this.getApplication()).getSyncanoInstance().setUser(null);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        this.finish();
    }
}
