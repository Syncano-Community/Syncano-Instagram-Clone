package com.solid9studio.instagram.user;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.Profile;
import com.syncano.library.data.SyncanoFile;

/**
 * Created by sebastian on 2016-11-28.
 */

public class InstagramProfile extends Profile {

    @SyncanoField(name = "avatar")
    protected SyncanoFile avatar;

    @SyncanoField(name = "name")
    protected String name;

    @SyncanoField(name = "surname")
    protected String surname;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SyncanoFile getAvatar() {
        return avatar;
    }

    public void setAvatar(SyncanoFile avatar) {
        this.avatar = avatar;
    }
}
