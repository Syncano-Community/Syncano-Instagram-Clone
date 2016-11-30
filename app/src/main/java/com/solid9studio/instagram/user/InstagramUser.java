package com.solid9studio.instagram.user;

import com.syncano.library.choice.SocialAuthBackend;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.SyncanoFile;

/**
 * Created by sebastian on 2016-11-28.
 */

public class InstagramUser extends AbstractUser<InstagramProfile> {

    public InstagramUser(String login, String pass) { super(login, pass); }

    public InstagramUser(SocialAuthBackend socialAuthBackend, String authToken) { super(socialAuthBackend, authToken); }

    public InstagramUser(String login, String pass, String name, String surname, SyncanoFile avatar)
    {
        super(login, pass);
        setProfile(new InstagramProfile());

        getProfile().name = name;
        getProfile().surname = surname;
        getProfile().avatar = avatar;
    }

    public InstagramUser() { }
}