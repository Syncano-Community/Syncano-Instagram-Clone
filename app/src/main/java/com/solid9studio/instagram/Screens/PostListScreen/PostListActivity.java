package com.solid9studio.instagram.screens.PostListScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.model.Post;
import com.solid9studio.instagram.screens.PostScreen.PostActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.post_list)
    RecyclerView recyclerView;

    private PostListAdapter adapter = new PostListAdapter();

    public static Intent getActivityIntent(Context context) {
        Intent intent = new Intent(context, PostListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadPosts();
            }
        });
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        startActivity(PostActivity.getActivityIntent(this, 1));
    }

    private void downloadPosts() {
        // Download mockup
        ArrayList<Post> posts = new ArrayList<>(3);
        for(int i = 0; i < 3; i++) {
            Post post = new Post();
            post.setId(i + 1);
            post.setText("This is post with id: " + post.getId());
            posts.add(post);
        }

        displayPosts(posts);
    }

    private void displayPosts(List<Post> posts) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setData(posts);
        adapter.notifyDataSetChanged();
    }
}
