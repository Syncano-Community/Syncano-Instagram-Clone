package com.solid9studio.instagram.screen.registerScreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.screen.postListScreen.PostListActivity;
import com.solid9studio.instagram.user.InstagramProfile;
import com.solid9studio.instagram.user.InstagramUser;
import com.solid9studio.instagram.utilities.Utilities;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    // UI references.
    @BindView(R.id.name)
    EditText mNameView;

    @BindView(R.id.surname)
    EditText mSurnameView;

    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;

    @BindView(R.id.password)
    EditText mPasswordView;

    @BindView(R.id.confirm_password)
    EditText mConfirmPasswordView;

    @BindView(R.id.login_progress)
    View mProgressView;

    @BindView(R.id.register_form)
    View mFormView;

    @BindView(R.id.user_avatar)
    ImageView mUserAvatar;

    private File avatarFile;

    public static Intent getActivityIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mConfirmPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Utilities.setSelectPictureListener(this, mUserAvatar);
    }

    @OnClick(R.id.register_button)
    public void onRegisterButton()
    {
        attemptRegister();
    }

    private void attemptRegister() {

        // Reset errors.
        mNameView.setError(null);
        mSurnameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);

        // Store values at the time of the register attempt.
        String name = mNameView.getText().toString();
        String surname = mSurnameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else if (!TextUtils.isEmpty(confirmPassword) && !confirmPassword.equals(password)) {
            mConfirmPasswordView.setError(getString(R.string.error_confirm_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            new RegisterUserTask().execute(email, password, name, surname);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void goToPostList() {
        startActivity(PostListActivity.getActivityIntent(this));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        avatarFile = Utilities.onSelectedPicutreFromGallery(this, requestCode, resultCode, data, mUserAvatar);
    }

    public class RegisterUserTask extends AsyncTask<String, Void, Void> {

        private InstagramUser newUser;

        @Override
        protected Void doInBackground(String... params) {

            newUser = new InstagramUser();
            newUser.setUserName(params[0]);
            newUser.setPassword(params[1]);

            Response<InstagramUser> responseRegister = newUser.register();

            if(responseRegister.isSuccess())
            {
                newUser.getProfile().setName(params[2]);
                newUser.getProfile().setSurname(params[3]);
                newUser.getProfile().setAvatar(new SyncanoFile(avatarFile));

                Response<InstagramProfile> responseSaveProfile = newUser.getProfile().save();

                if(responseSaveProfile.isSuccess()) {

                    goToPostList();
                }

                else
                {
                    Utilities.showToast(getApplicationContext(), responseRegister.getError());
                }
            }

            else {
                Utilities.showToast(getApplicationContext(), responseRegister.getError());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            showProgress(false);
            super.onPostExecute(aVoid);
        }
    }
}

