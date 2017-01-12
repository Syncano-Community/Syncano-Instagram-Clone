package com.solid9studio.instagram.screen.postScreen;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.solid9studio.instagram.model.InstaComment;
import com.solid9studio.instagram.Row;
import com.solid9studio.instagram.model.InstaPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by d.czlonka on 16/11/16.
 */

public class PostContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_POST = 1;
    private static final int TYPE_COMMENT = 2;

    private InstaPost post = null;
    private ArrayList<InstaComment> comments = new ArrayList<>();
    private ArrayList<Row> rows = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_POST:
                return PostContentRow.makeViewHolder(parent);
            case TYPE_COMMENT:
                return CommentRow.makeViewHolder(parent);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Row r = rows.get(position);
        if (r instanceof PostContentRow) {
            return TYPE_POST;
        } else if (r instanceof CommentRow) {
            return TYPE_COMMENT;
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

    public void setData(InstaPost post, List<InstaComment> newComments) {
        this.post = post;
        if (newComments != null) {
            comments = new ArrayList<>(newComments);
        } else {
            comments = new ArrayList<>();
        }

        generateRows();
        notifyDataSetChanged();
    }

    private void generateRows() {
        ArrayList<Row> newRows = new ArrayList<>();

        if (post != null) {
            newRows.add(new PostContentRow(0, post)); // Header

            for (InstaComment comment : comments) {
                newRows.add(new CommentRow(comment.getId(), comment));
            }
        }

        this.rows = newRows;
    }
}
