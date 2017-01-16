package com.solid9studio.instagram.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.constant.Constants;
import com.solid9studio.instagram.model.InstagramPost;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.Trace;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sebastian on 2016-11-30.
 */

public class Utilities {

    private static final String FIELD_TARGET = "target";
    public static final int SELECT_PICTURE = 1;

    public static void setSelectPictureListener(final Activity context, View view)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                context.startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
    }

    public static File onSelectedPicutreFromGallery(Context context, int requestCode, int resultCode, Intent data, ImageView imageView)
    {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (Build.VERSION.SDK_INT < 19) {

                    String selectedImagePath = getPath(context, selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    imageView.setImageBitmap(bitmap);
                }
                else {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = context.getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

                        if(image.getWidth() >= 4128)
                        {
                            showToast(context, "Image width is too large and can't be displayed.");
                        }

                        else if(image.getHeight() >= 2322)
                        {
                            showToast(context, "Image height is too large and can't be displayed.");
                        }

                        parcelFileDescriptor.close();
                        imageView.setImageBitmap(image);

                        Uri uri = data.getData();
                        return new File(getPath(context, uri));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    public static String getPath(Context context, Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    public static void showToast(Context ctx, String text)
    {
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(ctx, text, duration);
        toast.show();
    }

    public static void notifyLikedPost(final Context context, final InstagramPost post, final int userID, ImageView likeImageView, TextView likeText)
    {
        JsonObject params = new JsonObject();
        params.addProperty(FIELD_TARGET, post.getInstagramProfile().getPushUrl());

        if(post.getLikeCountList() == null)
        {
            post.setLikeCountList(new ArrayList<Double>());
        }

        post.getLikeCountList().add((double)userID);
        likeImageView.setImageResource(R.drawable.heart);
        likeText.setText(String.valueOf(post.getLikeCountList().size()) + " likes");

        ((Instagram) context.getApplicationContext()).getSyncanoInstance().runScript(Constants.NOTIFY_USER_SCRIPT_ID, params).sendAsync(new SyncanoCallback<Trace>() {
            @Override
            public void success(Response<Trace> response, Trace result) {

                post.save(new SyncanoCallback<SyncanoObject>() {
                    @Override
                    public void success(Response<SyncanoObject> response, SyncanoObject result) {

                    }

                    @Override
                    public void failure(Response<SyncanoObject> response) {
                        Utilities.showToast(context, response.getError());
                    }
                });
            }

            @Override
            public void failure(Response<Trace> response) {
                Utilities.showToast(context, response.getError());
            }
        });
    }
}
