package com.solid9studio.instagram.screen.postListScreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solid9studio.instagram.R;
import com.solid9studio.instagram.model.Post;
import com.solid9studio.instagram.Row;
import com.solid9studio.instagram.screen.postScreen.PostActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by d.czlonka on 16/11/16.
 */

public class PostRow extends Row {

    private Post post;

    public PostRow(long id, Post post) {
        super(id);
        this.post = post;
    }

    public static RecyclerView.ViewHolder makeViewHolder(ViewGroup parent) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_row, parent, false));
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        ViewHolder h = (ViewHolder) holder;
        h.topView.setTag(post);

        h.text.setText(post.getText());
    }

    // ==================== ViewHolder ==================== //

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.post_caption)
        TextView text;

        View topView;

        public ViewHolder(View itemView) {
            super(itemView);
            topView = itemView;
            topView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Post post = (Post)v.getTag();
            if (post != null) {
                context.startActivity(PostActivity.getActivityIntent(context, post.getId()));
            }
        }
    }
}
