package com.solid9studio.instagram.model.syncano;

import com.solid9studio.instagram.user.InstagramProfile;
import com.solid9studio.instagram.user.InstagramUser;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

/**
 * Created by Sebastian on 2017-01-10.
 */

@SyncanoClass(name = "instapost")
public class InstaPost extends SyncanoObject {

    public static final String FIELD_SUMMARY = "summary";
    public static final String FIELD_POST_IMAGE = "post_image";
    public static final String FIELD_POST_OWNER = "post_owner_id";

    @SyncanoField(name = FIELD_POST_IMAGE)
    private SyncanoFile postImage;

    @SyncanoField(name = FIELD_SUMMARY)
    private String postSummary;

    @SyncanoField(name = FIELD_POST_OWNER)
    private InstagramProfile instagramProfile;

    public SyncanoFile getPostImage() {
        return postImage;
    }

    public void setPostImage(SyncanoFile postImage) {
        this.postImage = postImage;
    }

    public String getPostSummary() {
        return postSummary;
    }

    public void setPostSummary(String postSummary) {
        this.postSummary = postSummary;
    }

    public InstagramProfile getInstagramProfile() {
        return instagramProfile;
    }

    public void setInstagramProfile(InstagramProfile instagramProfile) {
        this.instagramProfile = instagramProfile;
    }


    /*
    public int getPostOwnerId() {
        return postOwnerId;
    }

    public void setPostOwnerId(int postOwnerId) {
        this.postOwnerId = postOwnerId;
    }*/
}