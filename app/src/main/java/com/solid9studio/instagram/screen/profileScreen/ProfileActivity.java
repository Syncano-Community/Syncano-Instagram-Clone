package com.solid9studio.instagram.screen.profileScreen;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.user.InstagramUser;
import com.syncano.library.Syncano;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.syncano_name)
    EditText mUsernameView;

    @BindView(R.id.syncano_surname)
    EditText mSurnameView;

    @BindView(R.id.syncano_email)
    TextView mEmailVIew;

    @BindView(R.id.avatar_button)
    Button mAvatarButton;

    @BindView(R.id.update_profile_button)
    Button mUpdateProfileButton;

    @BindView(R.id.syncano_avatar)
    ImageView mAvatarView;

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

    private InstagramUser user;
    private Syncano syncano;
    private  File avatarFile;

    public static Intent getActivityIntent(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        mAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

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
    }

    private void updateProfile()
    {
        user.getProfile().setName(mUsernameView.getText().toString());
        user.getProfile().setSurname(mUsernameView.getText().toString());
        user.getProfile().setAvatar(new SyncanoFile(avatarFile));


        user.getProfile().save(new SyncanoCallback<SyncanoObject>() {
            @Override
            public void success(Response<SyncanoObject> response, SyncanoObject result) {
                syncano.setUser(user);
            }

            @Override
            public void failure(Response<SyncanoObject> response) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (Build.VERSION.SDK_INT < 19) {

                    selectedImagePath = getPath(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    mAvatarView.setImageBitmap(bitmap);
                }
                else {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        mAvatarView.setImageBitmap(image);

                        Uri uri = data.getData();
                        avatarFile = new File(getPath(uri));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
}