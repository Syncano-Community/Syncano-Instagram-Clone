package com.solid9studio.instagram.screen.profileScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.user.InstagramUser;
import com.solid9studio.instagram.utilities.Utilities;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.syncano.library.Syncano;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.name)
    EditText mUsernameView;

    @BindView(R.id.surname)
    EditText mSurnameView;

    @BindView(R.id.email)
    TextView mEmailVIew;

    @BindView(R.id.user_avatar)
    ImageView mAvatarView;

    @BindView(R.id.update_button)
    Button mUpdateProfileButton;

    @BindView(R.id.update_progress)
    View mProgressView;

    private InstagramUser user;
    private Syncano syncano;
    private File avatarFile;

    public static Intent getActivityIntent(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        Utilities.setSelectPictureListener(this, mAvatarView);
        mUpdateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        syncano = ((Instagram) this.getApplication()).getSyncanoInstance();

        user = (InstagramUser)syncano.getUser();
        mUsernameView.setText(user.getProfile().getName());
        mSurnameView.setText(user.getProfile().getSurname());
        mEmailVIew.setText(user.getUserName());

        String avatarLink = user.getProfile().getAvatar().getLink();

        if(avatarLink != null && avatarLink.isEmpty() == false)
        {
            Picasso.with(this).load(avatarLink).into(mAvatarView, new Callback() {
                @Override
                public void onSuccess() {
                    mProgressView.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    mProgressView.setVisibility(View.GONE);
                }
            });
        }
    }

    private void updateProfile()
    {
        mProgressView.setVisibility(View.VISIBLE);
        user.getProfile().setName(mUsernameView.getText().toString());
        user.getProfile().setSurname(mSurnameView.getText().toString());
        user.getProfile().setAvatar(new SyncanoFile(avatarFile));

        user.getProfile().save(new SyncanoCallback<SyncanoObject>() {
            @Override
            public void success(Response<SyncanoObject> response, SyncanoObject result) {
                syncano.setUser(user);
                mProgressView.setVisibility(View.GONE);
            }

            @Override
            public void failure(Response<SyncanoObject> response) {
                Utilities.showToast(getApplicationContext(), response.getError());
                mProgressView.setVisibility(View.GONE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        avatarFile = Utilities.onSelectedPicutreFromGallery(this, requestCode, resultCode, data, mAvatarView);
    }
}