package com.solid9studio.instagram.screen.postScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.model.Comment;
import com.solid9studio.instagram.model.Post;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostActivity extends BaseActivity {

    public static final String EXTRA_POST_ID = "EXTRA_POST_ID";

    @BindView(R.id.download_progress)
    ProgressBar downloadProgress;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.content_list)
    RecyclerView recyclerView;

    private long postId;
    private Post post;
    private PostContentAdapter adapter = new PostContentAdapter();

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadPost(postId);
            }
        });

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
        ArrayList<Comment> comments = new ArrayList<>(3);
        for(int i = 0; i < 8; i++) {
            Comment comment = new Comment();
            comment.setId(i + 1);
            comment.setText("This is comment with id: " + comment.getId());
            comments.add(comment);
        }

        displayComments(comments);
    }

    public void displayPost(Post post) {
        downloadProgress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setData(post, null);
    }

    public void displayComments(List<Comment> comments) {
        adapter.setData(post, comments);
    }
}
