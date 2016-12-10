package com.solid9studio.instagram.screen.loginScreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.screen.postListScreen.PostListActivity;
import com.solid9studio.instagram.screen.registerScreen.RegisterActivity;
import com.solid9studio.instagram.user.InstagramUser;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.choice.SocialAuthBackend;
import com.syncano.library.data.AbstractUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    // UI references.
    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;

    @BindView(R.id.password)
    EditText mPasswordView;

    @BindView(R.id.login_progress)
    View mProgressView;

    @BindView(R.id.login_form)
    View mLoginFormView;

    @BindView(R.id.social_login_button)
    LoginButton mLoginButton;

    private InstagramUser user;
    private CallbackManager callbackManager;
    private Response<InstagramUser> loginResponse;

    public static Intent getActivityIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));
        mLoginButton.registerCallback(callbackManager, callback);


        mEmailView.setText("test@test.pl");
        mPasswordView.setText("syncano");
        user = new InstagramUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_register:
                goToRegister();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.email_sign_in_button)
    public void onEmailSignInButton() {
        attemptLogin();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
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

            user.setUserName(email);
            user.setPassword(password);

            user.login(new SyncanoCallback<AbstractUser>() {
                @Override
                public void success(Response<AbstractUser> response, AbstractUser result) {
                    user = (InstagramUser) response.getData();
                    saveUser();
                    goToPostList();
                }

                @Override
                public void failure(Response<AbstractUser> response) {
                    showProgress(false);
                }
            });
        }
    }

    private void attemptLoginSocial(String accessToken, String email) {
        user.setUserName(email);

        user = new InstagramUser(SocialAuthBackend.FACEBOOK, accessToken); //"EAAZAeOKQV3oYBABDnfhxIAvJsd9ZA33gM1vu7W1SEAelNrMGNm8krr7LrqkrMU0KF3lG8ZCQXxg2cBCMZCaVTFLVwqSGieKKzpbHHactgTtuaOKCfpsfOm9cWzXcZADwl7IJcZBDT2Y5ebTC1XIxWkaepxW1HxgY1Hn4ieXL58hQZDZD");
        user.loginSocialUser(new SyncanoCallback<AbstractUser>() {
            @Override
            public void success(Response<AbstractUser> response, AbstractUser result) {
                user = (InstagramUser) response.getData();
                saveUser();
                goToPostList();
            }

            @Override
            public void failure(Response<AbstractUser> response) {
                showProgress(false);
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void goToPostList() {
        startActivity(PostListActivity.getActivityIntent(this));
    }

    private void goToRegister() {
        startActivity(RegisterActivity.getActivityIntent(this));
    }

    private void saveUser() {
        ((Instagram) this.getApplication()).getSyncanoInstance().setUser(user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
           final AccessToken accessToken = loginResult.getAccessToken();


            // Facebook Email address
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try {

                                String email = object.getString("email");
                                attemptLoginSocial(accessToken.getToken(), email);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };
}

