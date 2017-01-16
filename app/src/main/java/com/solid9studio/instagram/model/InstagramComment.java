package com.solid9studio.instagram.model;

import com.solid9studio.instagram.user.InstagramProfile;

import com.syncano.library.data.SyncanoObject;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;

/**
 * Class that corresponds to data structure on Syncano server side.
 */

@SyncanoClass(name = InstagramComment.CLASS_INSTAGRAM)
public class InstagramComment extends SyncanoObject {

    public static final String CLASS_INSTAGRAM  = "instacomment";
    public static final String FIELD_TEXT = "text";
    public static final String FIELD_POST_ID = "post_id";
    public static final String FIELD_COMMENT_OWNER = "post_owner";

    @SyncanoField(name = FIELD_TEXT)
    private String text;

    @SyncanoField(name = FIELD_POST_ID)
    private InstagramPost instagramPost;

    @SyncanoField(name = FIELD_COMMENT_OWNER)
    private InstagramProfile instagramProfile;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public InstagramPost getInstagramPost() {
        return instagramPost;
    }

    public void setInstagramPost(InstagramPost instagramPost) {
        this.instagramPost = instagramPost;
    }

    public InstagramProfile getInstagramProfile() {
        return instagramProfile;
    }

    public void setInstagramProfile(InstagramProfile instagramProfile) {
        this.instagramProfile = instagramProfile;
    }
}
