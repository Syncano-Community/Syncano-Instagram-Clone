package com.solid9studio.instagram.user;

import com.syncano.library.data.AbstractUser;

/**
 * Created by sebastian on 2016-11-28.
 */

public class InstagramUser extends AbstractUser<InstagramProfile> {

    public InstagramUser(String login, String pass) { super(login, pass); }

    public InstagramUser() { }
}