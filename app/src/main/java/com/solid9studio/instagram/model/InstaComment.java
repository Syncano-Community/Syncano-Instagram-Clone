package com.solid9studio.instagram.model;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

/**
 * Created by d.czlonka on 16/11/16.
 */

@SyncanoClass(name = "instacomment")
public class InstaComment extends SyncanoObject {

    public static final String FIELD_TEXT = "text";

    @SyncanoField(name = FIELD_TEXT)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
