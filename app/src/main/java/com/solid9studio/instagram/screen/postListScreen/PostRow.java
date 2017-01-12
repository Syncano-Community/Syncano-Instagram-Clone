package com.solid9studio.instagram.screen.postListScreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.solid9studio.instagram.R;
import com.solid9studio.instagram.Row;
import com.solid9studio.instagram.model.InstaPost;
import com.solid9studio.instagram.screen.postScreen.PostActivity;
import com.solid9studio.instagram.screen.postScreen.PostContentRow;
import com.solid9studio.instagram.view.SquareImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by d.czlonka on 16/11/16.
 */

public class PostRow extends Row {

    private InstaPost post;

    public PostRow(long id, InstaPost post) {
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

        h.text.setText(post.getPostSummary());
        h.author.setText(post.getInstagramProfile().getName());
        h.creationDate.setText(PostContentRow.SDF.format(post.getCreatedAt()));
        Picasso.with(h.topView.getContext()).load(post.getPostImage().getLink()).into(h.image);
        Picasso.with(h.topView.getContext()).load(post.getInstagramProfile().getAvatar().getLink()).into(h.userAvatarImage);
    }

    // ==================== ViewHolder ==================== //

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.post_caption)
        TextView text;

        @BindView(R.id.post_user_name)
        TextView author;

        @BindView(R.id.post_time_text)
        TextView creationDate;

        @BindView(R.id.post_image)
        SquareImageView image;

        @BindView(R.id.post_user_avatar)
        ImageView userAvatarImage;

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
            InstaPost post = (InstaPost)v.getTag();
            if (post != null) {
                context.startActivity(PostActivity.getActivityIntent(context, post.getId()));
            }
        }
    }
}
