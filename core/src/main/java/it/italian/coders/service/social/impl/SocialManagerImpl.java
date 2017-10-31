package it.italian.coders.service.social.impl;

import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;
import it.italian.coders.service.social.SocialManager;
import it.italian.coders.service.social.facebook.FacebookSocialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialManagerImpl implements SocialManager{

    @Autowired
    FacebookSocialManager facebookSocialManager;

    @Override
    public User updInsSocialUser(SocialEnum socialEnum, String userId, String accessToken){
        User user = null;
        switch (socialEnum){
            case Facebook:
                user = facebookSocialManager.updInsSocialUser(accessToken);
                break;
        }

        return user;
    }
}
