package com.solid9studio.instagram.screen.createPostScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.model.InstagramPost;
import com.solid9studio.instagram.screen.postListScreen.PostListActivity;
import com.solid9studio.instagram.user.InstagramProfile;
import com.solid9studio.instagram.utilities.Utilities;

import com.squareup.picasso.Picasso;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import java.io.ByteArrayOutputStream;
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

    @BindView(R.id.progress_bar_share_post)
    ProgressBar progressBarSharePost;

    private Menu menu;

    private File avatarFile;

    private boolean isUploading;

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
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                if(isUploading == false) {
                    sharePost();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sharePost() {
        menu.getItem(0).setEnabled(false);
        progressBarSharePost.setVisibility(View.VISIBLE);

        new SharePicture().execute(imageSummaryText.getText().toString());
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
        progressBarSharePost.setVisibility(View.INVISIBLE);
        Utilities.showToast(getApplicationContext(), "Post added!");

        Intent intent = new Intent(this, PostListActivity.class);
        startActivity(intent);
    }

    public Bitmap getBitmapDrawable()
    {
        return ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
    }

    public class SharePicture extends AsyncTask<String, Void, Void> {

        private Response<InstagramPost> response;

        @Override
        protected Void doInBackground(String... params) {

            isUploading = true;
            Instagram instagram = ((Instagram) getApplication());
            InstagramProfile instagramProfile = instagram.getUser().getProfile();
            InstagramPost instaPost = new InstagramPost();

            instaPost.setPostSummary(params[0]);

            Bitmap bitmap = getBitmapDrawable();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            instaPost.setPostImage(new SyncanoFile(byteArray));
            instaPost.setInstagramProfile(instagramProfile);

            response = instaPost.save();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(response.isSuccess()) {
                onPictureSent();
            }

            else {
                Utilities.showToast(getApplicationContext(), response.getError());
            }

            isUploading = false;
            menu.getItem(0).setEnabled(true);

            super.onPostExecute(aVoid);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {

            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
      if(isUploading)
      {
          return;
      }
        else {
          super.onBackPressed();
      }

    }
}
