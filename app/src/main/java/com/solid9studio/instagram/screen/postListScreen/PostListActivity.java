package com.solid9studio.instagram.screen.postListScreen;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.constant.Constants;
import com.solid9studio.instagram.model.InstagramPost;
import com.solid9studio.instagram.push.GetApplicationTokenTask;
import com.solid9studio.instagram.screen.createPostScreen.CreatePostActivity;
import com.solid9studio.instagram.screen.postScreen.PostActivity;
import com.solid9studio.instagram.screen.profileScreen.ProfileActivity;
import com.solid9studio.instagram.screen.settingsScreen.SettingsActivity;
import com.solid9studio.instagram.user.InstagramUser;
import com.solid9studio.instagram.utilities.Utilities;
import com.syncano.library.Syncano;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.simple.RequestBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostListActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_USER_FILTER = "user_id";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.post_list)
    RecyclerView recyclerView;

    private PostListAdapter adapter = new PostListAdapter(this);
    private Syncano syncano;
    private int userIdFilter; // Used to list only this user posts.

    public static Intent getActivityIntent(Context context) {
        Intent intent = new Intent(context, PostListActivity.class);
        return intent;
    }

    public static Intent getActivityIntent(Context context, int userIdFilter) {
        Intent intent = new Intent(context, PostListActivity.class);
        intent.putExtra(EXTRA_USER_FILTER, userIdFilter);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Filter by user
        userIdFilter = getIntent().getIntExtra(EXTRA_USER_FILTER, 0);

        if (userIdFilter > 0)
            setTitle(R.string.action_my_posts);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadPosts();
            }
        });

        Instagram instagram = (Instagram) this.getApplication();
        syncano = instagram.getSyncanoInstance();
        String token = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.TOKEN, "");

        if (token.isEmpty()) {
            new GetApplicationTokenTask(getApplicationContext(), instagram.getUser()).execute();
        }

        downloadPosts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_my_posts:
                goToMyPosts();
                return true;
            case R.id.action_profile:
                goToProfile();
                return true;
            case R.id.action_settings:
                goToSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        startActivity(CreatePostActivity.getActivityIntent(this));
    }

    private void downloadPosts() {
        RequestBuilder<InstagramPost> request = Syncano.please(InstagramPost.class);

        if (userIdFilter > 0) {
            request.where().eq(InstagramPost.FIELD_POST_OWNER, userIdFilter);
        }

        request.orderBy(SyncanoObject.FIELD_CREATED_AT, SortOrder.DESCENDING).get(new SyncanoListCallback<InstagramPost>() {
              @Override
              public void success(ResponseGetList<InstagramPost> response, List<InstagramPost> result) {

                  new FetchPostProfiles().execute(result);
              }

              @Override
              public void failure(ResponseGetList<InstagramPost> response) {
                  Utilities.showToast(getApplicationContext(), response.getError());
            }
          });
    }

    @Override
    public void onRestart() {

        downloadPosts();
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
    }

    public void goToSettings() {
        startActivity(SettingsActivity.getActivityIntent(this));
    }

    public void goToProfile() {
        startActivity(ProfileActivity.getActivityIntent(this));
    }

    public void goToMyPosts() {
        InstagramUser user = (InstagramUser)syncano.getUser();
        if (user != null && user.getId() > 0) {
            startActivity(getActivityIntent(this, user.getId()));
        }
    }

    private void displayPosts(List<InstagramPost> posts) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setData(posts);
        adapter.notifyDataSetChanged();
    }

    /**
     * This Async Task is responsible for getting user info for every post.
     */
    private class FetchPostProfiles extends AsyncTask<List<InstagramPost> , Void, List<InstagramPost>>
    {
        @Override
        protected List<InstagramPost> doInBackground(List<InstagramPost> ...params) {

            List<InstagramPost> result = params[0];

            for(int i = 0; i <result.size(); i++)
            {
               result.get(i).getInstagramProfile().fetch();
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<InstagramPost> result) {

            displayPosts(result);
        }
    }

    @Override
    public void onClick(View v) {
        InstagramPost post = (InstagramPost)v.getTag();
        Instagram instagram = (Instagram) this.getApplication();

        if (v.getId() == R.id.like_container) {
            if (post != null && post.isLikedByMe(instagram.getUser().getProfile().getId()) == false) {

                Utilities.notifyLikedPost(this, post
                        ,instagram.getUser().getProfile().getId()
                        ,(ImageView) v.findViewById(R.id.like_icon)
                        ,(TextView) v.findViewById(R.id.like_text));
            }
        } else if (v.getId() == R.id.post_top_view) {
            // On post click
            if (post != null) {
                startActivity(PostActivity.getActivityIntent(this, post.getId()));
            }
        }
    }
}
