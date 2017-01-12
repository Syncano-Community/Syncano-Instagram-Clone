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
import com.solid9studio.instagram.model.InstaComment;
import com.solid9studio.instagram.model.InstaPost;
import com.solid9studio.instagram.user.InstagramProfile;
import com.syncano.library.Syncano;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.SyncanoObject;

import java.util.ArrayList;
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

    private int postId;
    private InstaPost post;

    private PostContentAdapter adapter = new PostContentAdapter();

    public static Intent getActivityIntent(Context context, int postId) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(EXTRA_POST_ID, postId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        postId = getIntent().getExtras().getInt(EXTRA_POST_ID);

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

    public void downloadPost(int postId) {
        InstaPost instaPost = new InstaPost();
        instaPost.setId(postId);

        instaPost.fetch(new SyncanoCallback<InstaPost>() {
            @Override
            public void success(Response<InstaPost> response, InstaPost result) {

                post = result;

                result.getInstagramProfile().fetch(new SyncanoCallback<SyncanoObject>() {
                    @Override
                    public void success(Response<SyncanoObject> response, SyncanoObject result) {

                        post.setInstagramProfile((InstagramProfile) result);
                        displayPost(post);

                        downloadComments(post.getId());
                    }

                    @Override
                    public void failure(Response<SyncanoObject> response) { }
                });
            }

            @Override
            public void failure(Response<InstaPost> response) { }
        });
    }

    public void downloadComments(int postId) {

        Syncano.please(InstaComment.class).where().in("post_id", new Integer[] { postId }).get(new SyncanoCallback<List<InstaComment>>() {
            @Override
            public void success(Response<List<InstaComment>> response, List<InstaComment> result) {
                displayComments(result);
            }

            @Override
            public void failure(Response<List<InstaComment>> response) {

            }
        });
    }

    public void displayPost(InstaPost post) {
        downloadProgress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setData(post, null);
    }

    public void displayComments(List<InstaComment> comments) {
        adapter.setData(post, comments);
    }
}
