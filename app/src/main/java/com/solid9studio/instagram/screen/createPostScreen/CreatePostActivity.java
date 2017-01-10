package com.solid9studio.instagram.screen.createPostScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.model.syncano.InstaPost;
import com.solid9studio.instagram.user.InstagramUser;
import com.solid9studio.instagram.utilities.Utilities;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreatePostActivity extends BaseActivity {

    @BindView(R.id.image_send)
    ImageView mImageView;

    @BindView(R.id.text_post_summary)
    EditText imageSummaryText;

    @BindView(R.id.image_send_text_view)
    TextView imageText;

    private File avatarFile;

    public static Intent getActivityIntent(Context context) {
        Intent intent = new Intent(context, CreatePostActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);
        Utilities.setSelectPictureListener(this, mImageView);
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
        InstaPost instaPost = new InstaPost();
        instaPost.setPostSummary(imageSummaryText.getText().toString());
        instaPost.setPostImage(new SyncanoFile(avatarFile));

        instaPost.setPostOwner(((Instagram) this.getApplication()).getSyncanoInstance().getUser().getId());

        instaPost.save(new SyncanoCallback<SyncanoObject>() {
            @Override
            public void success(Response<SyncanoObject> response, SyncanoObject result) {
                onPictureSent();
            }

            @Override
            public void failure(Response<SyncanoObject> response) {
                onPictureSent();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        avatarFile = Utilities.onSelectedPicutreFromGallery(this, requestCode, resultCode, data, mImageView);
        imageText.setText("");
    }

    private void onPictureSent()
    {
        imageText.setText("Add...");
        imageSummaryText.setText("");
        mImageView.setImageDrawable(null);
        avatarFile = null;
    }
}
