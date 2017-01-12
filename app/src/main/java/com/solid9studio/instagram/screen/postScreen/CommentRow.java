package com.solid9studio.instagram.screen.postScreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solid9studio.instagram.R;
import com.solid9studio.instagram.Row;
import com.solid9studio.instagram.model.InstaComment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by d.czlonka on 16/11/16.
 */

public class CommentRow extends Row {

    private InstaComment comment;

    public CommentRow(long id, InstaComment comment) {
        super(id);
        this.comment = comment;
    }

    public static RecyclerView.ViewHolder makeViewHolder(ViewGroup parent) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_row, parent, false));
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        ViewHolder h = (ViewHolder) holder;
        h.topView.setTag(comment);

        h.text.setText(comment.getText());
        h.authorText.setText(comment.getInstagramProfile().getName());
    }

    // ==================== ViewHolder ==================== //

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.comment_text)
        TextView text;

        @BindView(R.id.comment_user)
        TextView authorText;

        View topView;

        public ViewHolder(View itemView) {
            super(itemView);
            topView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
