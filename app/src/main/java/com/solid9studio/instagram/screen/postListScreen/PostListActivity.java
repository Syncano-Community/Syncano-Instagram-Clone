package com.solid9studio.instagram.screen.postListScreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.solid9studio.instagram.BaseActivity;
import com.solid9studio.instagram.R;
import com.solid9studio.instagram.model.syncano.InstaPost;
import com.solid9studio.instagram.screen.createPostScreen.CreatePostActivity;
import com.solid9studio.instagram.screen.profileScreen.ProfileActivity;
import com.solid9studio.instagram.screen.settingsScreen.SettingsActivity;
import com.syncano.library.Syncano;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoListCallback;

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

        Syncano.please(InstaPost.class).get(new SyncanoListCallback<InstaPost>() {
              @Override
              public void success(ResponseGetList<InstaPost> response, List<InstaPost> result) {
                  List<InstaPost> postList = response.getData();
                 // ArrayList<Post> posts = new ArrayList<>();

                  displayPosts(result);
                  /*
                  for (int i = 0; i< postList.size(); i++)
                  {
                      InstaPost instaPost = postList.get(i);
                      Post post = new Post();
                      post.setId(i + 1);
                      post.setText(instaPost.getPostSummary());
                      post.setCreatedAt(instaPost.getCreatedAt());
                      post.setImageUrl(instaPost.getPostImage().getLink());
                      post.setUserId(instaPost.getPostOwnerId());

                      posts.add(post);

                  }*/
              }

              @Override
              public void failure(ResponseGetList<InstaPost> response) {
            }
          });
    }

    public void goToSettings() {
        startActivity(SettingsActivity.getActivityIntent(this));
    }

    public void goToProfile() {
        startActivity(ProfileActivity.getActivityIntent(this));
    }

    private void displayPosts(List<InstaPost> posts) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setData(posts);
        adapter.notifyDataSetChanged();
    }
}
