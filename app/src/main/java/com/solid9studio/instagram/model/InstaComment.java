package com.solid9studio.instagram.model;

import com.solid9studio.instagram.user.InstagramProfile;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

/**
 * Created by d.czlonka on 16/11/16.
 */

@SyncanoClass(name = "instacomment")
public class InstaComment extends SyncanoObject {

    public static final String FIELD_TEXT = "text";
    public static final String FIELD_POST_ID = "post_id";
    public static final String FIELD_COMMENT_OWNER = "post_owner";

    @SyncanoField(name = FIELD_TEXT)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @SyncanoField(name = FIELD_POST_ID)
    private InstaPost instaPost;

    @SyncanoField(name = FIELD_COMMENT_OWNER)
    private InstagramProfile instagramProfile;

    public InstaPost getInstaPost() {
        return instaPost;
    }

    public void setInstaPost(InstaPost instaPost) {
        this.instaPost = instaPost;
    }

    public InstagramProfile getInstagramProfile() {
        return instagramProfile;
    }

    public void setInstagramProfile(InstagramProfile instagramProfile) {
        this.instagramProfile = instagramProfile;
    }
}
