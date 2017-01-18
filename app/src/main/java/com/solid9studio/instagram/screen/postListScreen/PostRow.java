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
import com.solid9studio.instagram.application.Instagram;
import com.solid9studio.instagram.model.InstagramPost;
import com.solid9studio.instagram.view.SquareImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by d.czlonka on 16/11/16.
 */

public class PostRow extends Row {

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm");
    private InstagramPost post;
    private Context context;

    public PostRow(long id, InstagramPost post, Context context) {
        super(id);
        this.post = post;
        this.context = context;
    }

    public static RecyclerView.ViewHolder makeViewHolder(ViewGroup parent) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_row, parent, false));
    }

    @Override
    public void bindView(RecyclerView.ViewHolder holder) {
        ViewHolder h = (ViewHolder) holder;
        h.topView.setTag(post);
        h.likeContainer.setTag(post);
        Instagram instagram = (Instagram) context.getApplicationContext();

        h.text.setText(post.getPostSummary());
        h.author.setText(post.getInstagramProfile().getName());
        h.creationDate.setText(SDF.format(post.getCreatedAt()));

        if(post.getPostImage() != null && post.getPostImage().getLink() != null)
        {
            Picasso.with(h.topView.getContext()).load(post.getPostImage().getLink()).into(h.image);
        }

        if(post.getInstagramProfile().getAvatar() != null && post.getInstagramProfile().getAvatar().getLink() != null)
        {
            Picasso.with(h.topView.getContext()).load(post.getInstagramProfile().getAvatar().getLink()).into(h.userAvatarImage);
        }

        int likesCount = 0;

        if(post.getLikeCountList() != null)
        {
            likesCount = post.getLikeCountList().size();
        }

        setLikes(h, likesCount, post.isLikedByMe(instagram.getUser().getProfile().getId()));
    }

    private void setLikes(ViewHolder h, int likeCount, boolean isLikedByMe) {

        if (isLikedByMe) {
            h.likeIcon.setImageResource(R.drawable.heart);
        } else {
            h.likeIcon.setImageResource(R.drawable.heart_gray);
        }

        h.likeText.setText(String.valueOf(likeCount) + " likes");
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

        @BindView(R.id.like_container)
        View likeContainer;

        @BindView(R.id.like_icon)
        ImageView likeIcon;

        @BindView(R.id.like_text)
        TextView likeText;

        View topView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            topView = itemView;
            topView.setOnClickListener(this);
            likeContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            if (context instanceof View.OnClickListener) {
                // Forward onClick to activity.
                ((View.OnClickListener)context).onClick(v);
            }
        }
    }
}
