package com.solid9studio.instagram.screen.postScreen;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;
import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.model.InstagramComment;
import com.solid9studio.instagram.model.InstagramPost;
import com.solid9studio.instagram.user.InstagramProfile;
import com.solid9studio.instagram.utilities.Utilities;
import com.syncano.library.Syncano;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.Trace;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_POST_ID = "EXTRA_POST_ID";
    public static final String FIELD_POST_OWNER_ID = "post_id";

    @BindView(R.id.download_progress)
    ProgressBar downloadProgress;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.content_list)
    RecyclerView recyclerView;

    @BindView(R.id.submit_comment)
    Button submitCommentButton;

    @BindView(R.id.comment_edit_text)
    EditText commentEditText;

    private int postId;
    private InstagramPost post;

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
        InstagramPost instaPost = new InstagramPost();
        instaPost.setId(postId);

        instaPost.fetch(new SyncanoCallback<InstagramPost>() {
            @Override
            public void success(Response<InstagramPost> response, InstagramPost result) {

                post = result;

                result.getInstagramProfile().fetch(new SyncanoCallback<SyncanoObject>() {
                    @Override
                    public void success(Response<SyncanoObject> response, SyncanoObject result) {

                        post.setInstagramProfile((InstagramProfile) result);
                        displayPost(post);

                        downloadComments(post.getId());
                    }

                    @Override
                    public void failure(Response<SyncanoObject> response) {
                        Utilities.showToast(getApplicationContext(), response.getError());
                    }
                });
            }

            @Override
            public void failure(Response<InstagramPost> response) {
                Utilities.showToast(getApplicationContext(), response.getError());
            }
        });
    }

    public void downloadComments(int postId) {

        Syncano.please(InstagramComment.class).orderBy(SyncanoObject.FIELD_CREATED_AT, SortOrder.DESCENDING).where().in(FIELD_POST_OWNER_ID, new Integer[]{postId}).get(new SyncanoCallback<List<InstagramComment>>() {
            @Override
            public void success(Response<List<InstagramComment>> response, List<InstagramComment> result) {
                new FetchPostsComments().execute(result);
            }

            @Override
            public void failure(Response<List<InstagramComment>> response) {
                Utilities.showToast(getApplicationContext(), response.getError());
            }
        });
    }

    public void displayPost(InstagramPost post) {
        downloadProgress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        adapter.setData(post, null);
    }

    public void displayComments(List<InstagramComment> comments) {
        adapter.setData(post, comments);
    }

    @OnClick(R.id.submit_comment)
    public void commentButtonOnClickListener() {
        if (validateComment()) {
            InstagramComment instaComment = new InstagramComment();
            InstagramPost instaPost = new InstagramPost();
            instaPost.setId(postId);

            Syncano syncano = ((Instagram) this.getApplication()).getSyncanoInstance();
            InstagramProfile instagramProfile = (InstagramProfile) syncano.getUser().getProfile();
            instaComment.setInstagramProfile(instagramProfile);

            instaComment.setInstagramPost(post);
            instaComment.setText(commentEditText.getText().toString());
            commentEditText.setText(null);

            instaComment.save(new SyncanoCallback<SyncanoObject>() {
                @Override
                public void success(Response<SyncanoObject> response, SyncanoObject result) {
                    downloadComments(postId);
                    notifyCommentedPost(post.getInstagramProfile().getPushUrl());
                }

                @Override
                public void failure(Response<SyncanoObject> response) {
                    Utilities.showToast(getApplicationContext(), response.getError());
                }
            });
        }
    }

    private boolean validateComment() {
        return commentEditText.getText().length() >= 1;
    }

    private class FetchPostsComments extends AsyncTask<List<InstagramComment>, Void, List<InstagramComment>> {
        @Override
        protected List<InstagramComment> doInBackground(List<InstagramComment>... params) {

            List<InstagramComment> result = params[0];

            for (int i = 0; i < result.size(); i++) {
                result.get(i).getInstagramProfile().fetch();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<InstagramComment> result) {
            displayComments(result);
        }
    }

    @Override
    public void onClick(View v) {
        InstagramPost post = (InstagramPost) v.getTag();

        if (v.getId() == R.id.like_container) {
            if (post != null && post.isLikedByMe() == false) {
                notifyCommentedPost(post.getInstagramProfile().getPushUrl());
            }
        }
    }

    private void notifyCommentedPost(String token) {
        JsonObject params = new JsonObject();
        params.addProperty("target", token);

        Instagram instagram = (Instagram) this.getApplication();
        post.getLikeCountList().add((double)instagram.getUser().getId());

        post.save(new SyncanoCallback<SyncanoObject>() {
            @Override
            public void success(Response<SyncanoObject> response, SyncanoObject result) {
                
            }

            @Override
            public void failure(Response<SyncanoObject> response) {
                Utilities.showToast(getApplicationContext(), response.getError());
            }
        });

        ((Instagram) this.getApplication()).getSyncanoInstance().runScript(1, params).sendAsync(new SyncanoCallback<Trace>() {
            @Override
            public void success(Response<Trace> response, Trace result) {

            }

            @Override
            public void failure(Response<Trace> response) {
                Utilities.showToast(getApplicationContext(), response.getError());
            }
        });
    }
}
