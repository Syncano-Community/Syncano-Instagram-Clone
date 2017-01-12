package com.solid9studio.instagram.screen.postListScreen;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.solid9studio.instagram.Row;
import com.solid9studio.instagram.model.InstaPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.czlonka on 16/11/16.
 */

public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_POST = 1;

    private ArrayList<InstaPost> posts = new ArrayList<>();
    private ArrayList<Row> rows = new ArrayList<>();

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

    public void setData(List<InstaPost> newPosts) {
        if (newPosts != null) {
            posts = new ArrayList<>(newPosts);
        } else {
            posts = new ArrayList<>();
        }

        generateRows();
    }

    private void generateRows() {

        ArrayList<Row> newRows = new ArrayList<>();

        for (InstaPost post : posts) {
            newRows.add(new PostRow(post.getId(), post));
        }

        this.rows = newRows;
    }
}
