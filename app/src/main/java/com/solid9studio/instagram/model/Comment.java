package com.solid9studio.instagram.model;

/**
 * Created by d.czlonka on 16/11/16.
 */

public class Comment extends BaseData {

    public String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
