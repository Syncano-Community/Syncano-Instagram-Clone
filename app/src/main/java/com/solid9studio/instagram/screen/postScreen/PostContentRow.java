package com.solid9studio.instagram.screen.postScreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.solid9studio.instagram.R;
import com.solid9studio.instagram.Row;
import com.solid9studio.instagram.model.Post;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by d.czlonka on 16/11/16.
 */

public class PostContentRow extends Row {

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");

    private Post post;

    public PostContentRow(long id, Post post) {
        super(id);
        this.post = post;
    }

    public static RecyclerView.ViewHolder makeViewHolder(ViewGroup parent) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.post_content, parent, false));
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        ViewHolder h = (ViewHolder) holder;
        h.topView.setTag(post);

        h.timeText.setText(SDF.format(post.getCreatedAt()));
        h.postCaption.setText(post.getText());
        Picasso.with(h.topView.getContext()).load(post.getImageUrl()).into(h.postImage);
    }

    // ==================== ViewHolder ==================== //

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.post_user_avatar)
        ImageView userAvatar;

        @BindView(R.id.post_user_name)
        TextView userName;

        @BindView(R.id.post_time_text)
        TextView timeText;

        @BindView(R.id.post_image)
        ImageView postImage;

        @BindView(R.id.post_caption)
        TextView postCaption;

        View topView;

        public ViewHolder(View itemView) {
            super(itemView);
            topView = itemView;
            ButterKnife.bind(this, itemView);
        }

    }
}
