package com.solid9studio.instagram.model;

import java.util.Date;

/**
 * Created by d.czlonka on 16/11/16.
 */

public class Post extends BaseData {

    private long userId;
    private Date createdAt;
    private String imageUrl;
    private String text;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
