package com.solid9studio.instagram.screen.postListScreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.solid9studio.instagram.Row;
import com.solid9studio.instagram.model.InstagramPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.czlonka on 16/11/16.
 */

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_POST = 1;

    private ArrayList<InstagramPost> posts = new ArrayList<>();
    private ArrayList<Row> rows = new ArrayList<>();
    private Context context;

    public PostListAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_POST:
                return PostRow.makeViewHolder(parent);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Row r = rows.get(position);
        if (r instanceof PostRow) {
            return TYPE_POST;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Row row = rows.get(position);
        row.bindView(holder);
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public void setData(List<InstagramPost> newPosts) {
        if (newPosts != null) {
            posts = new ArrayList<>(newPosts);
        } else {
            posts = new ArrayList<>();
        }

        generateRows();
    }

    private void generateRows() {

        ArrayList<Row> newRows = new ArrayList<>();

        for (InstagramPost post : posts) {
            newRows.add(new PostRow(post.getId(), post, context));
        }

        this.rows = newRows;
    }
}
