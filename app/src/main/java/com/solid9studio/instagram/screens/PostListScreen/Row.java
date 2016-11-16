package com.solid9studio.instagram.screens.PostListScreen;

import android.support.v7.widget.RecyclerView;

/**
 * Created by d.czlonka on 16/11/16.
 */

public abstract class Row {

    private long id;

    public Row(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public abstract void bindView(RecyclerView.ViewHolder holder);
}
