package com.solid9studio.instagram.user;

import com.syncano.library.choice.SocialAuthBackend;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.SyncanoFile;

public class InstagramUser extends AbstractUser<InstagramProfile> {

    public InstagramUser(SocialAuthBackend socialAuthBackend, String authToken) { super(socialAuthBackend, authToken); }

    public InstagramUser() { super(); }
}