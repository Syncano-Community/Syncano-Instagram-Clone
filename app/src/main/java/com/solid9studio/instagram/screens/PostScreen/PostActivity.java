package com.solid9studio.instagram.screens.PostScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.model.Comment;
import com.solid9studio.instagram.model.Post;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostActivity extends BaseActivity {

    public static final String EXTRA_POST_ID = "EXTRA_POST_ID";
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");

    @BindView(R.id.download_progress)
    ProgressBar downloadProgress;

    @BindView(R.id.post_container)
    View postContainer;

    @BindView(R.id.post_user_avatar)
    ImageView userAvatar;

    @BindView(R.id.post_user_name)
    TextView userName;

    @BindView(R.id.post_time_text)
    TextView timeText;

    @BindView(R.id.post_image)
    ImageView postImage;

    @BindView(R.id.post_caption)
    TextView postCaption;

    SwipeRefreshLayout swipeRefreshComments;

    RecyclerView commentList;

    private long postId;
    private Post post;

    public static Intent getActivityIntent(Context context, long postId) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(EXTRA_POST_ID, postId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        postId = getIntent().getExtras().getLong(EXTRA_POST_ID);
        downloadPost(postId);
    }

    public void downloadPost(long postId) {
        Post newPost = new Post();
        newPost.setId(postId);
        newPost.setUserId(postId);
        newPost.setCreatedAt(Calendar.getInstance().getTime());
        newPost.setImageUrl("http://www.walldevil.com/wallpapers/a73/wallpaper-football-warsaw-stadium-national-hobbit.jpg");
        newPost.setText("Caption under the image.");

        if (newPost != null) {
            post = newPost;
            displayPost(newPost);
            downloadComments(post.getId());
        }
    }

    public void downloadComments(long postId) {

    }

    public void displayPost(Post post) {
        postContainer.setVisibility(View.VISIBLE);
        downloadProgress.setVisibility(View.GONE);
        timeText.setText(SDF.format(post.getCreatedAt()));
        postCaption.setText(post.getText());
        Picasso.with(this).load(post.getImageUrl()).into(postImage);
    }

    public void displayComments(List<Comment> comments) {

    }
}
