package com.solid9studio.instagram.model;

import com.solid9studio.instagram.user.InstagramProfile;

import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;

import java.util.List;

/**
 * Class that corresponds to data structure on Syncano server side.
 */

@SyncanoClass(name = InstagramPost.CLASS_INSTAGRAM_POST)
public class InstagramPost extends SyncanoObject {

    public static final String CLASS_INSTAGRAM_POST= "instapost";
    public static final String FIELD_SUMMARY = "summary";
    public static final String FIELD_POST_IMAGE = "post_image";
    public static final String FIELD_POST_OWNER = "post_owner_id";
    public static final String FIELD_POST_LIKE_USERS = "like_users";

    @SyncanoField(name = FIELD_POST_IMAGE)
    private SyncanoFile postImage;

    @SyncanoField(name = FIELD_SUMMARY)
    private String postSummary;

    @SyncanoField(name = FIELD_POST_OWNER)
    private InstagramProfile instagramProfile;

    @SyncanoField(name = FIELD_POST_LIKE_USERS)
    private List<Double> likeCountList;

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

    public List<Double> getLikeCountList() {
        return likeCountList;
    }

    public void setLikeCountList(List<Double> likeCountList) {
        this.likeCountList = likeCountList;
    }

    public boolean isLikedByMe(int myId)
    {
        if(getLikeCountList() != null)
        {
            for(int i = 0; i < getLikeCountList().size(); i++)
            {
                int toCompare = getLikeCountList().get(i).intValue();

                if(myId == toCompare)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
