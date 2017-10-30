package it.italian.coders.service.authentication.impl;

import it.italian.coders.dao.authentication.UserDao;
import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;
import it.italian.coders.service.authentication.UserManager;
import it.italian.coders.service.social.facebook.FacebookSocialManager;
import it.italian.coders.service.social.facebook.impl.FacebookSocialManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserDao userDao;

    @Autowired
    private FacebookSocialManager facebookSocialManager;

    @Override
    public User findByUsernameIgnoreCase(String username) {
        return userDao.findByUsernameIgnoreCase(username);
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public User findByUsername(String username, SocialEnum socialEnum) {
        User user = null;
        switch (socialEnum){
            case None:
                user = userDao.findByUsername(username);
                break;
            case Facebook:
                user = facebookSocialManager.findByUserByAccessToken(username,true);
                break;

        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmailIgnoreCase(email);
    }


}
