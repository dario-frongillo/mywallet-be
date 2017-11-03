package it.italian.coders.service.social.twitter.impl;

import it.italian.coders.dao.authentication.UserDao;
import it.italian.coders.model.authentication.Authorities;
import it.italian.coders.model.authentication.GenderEnum;
import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;
import it.italian.coders.model.social.SocialUtils;
import it.italian.coders.service.social.twitter.TwitterSocialManger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static it.italian.coders.model.social.SocialUtils.USER_SOCIAL_PASSWORD;

@Service
public class TwitterSocialMangerImpl implements TwitterSocialManger {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User updInsSocialUser(String accessToken) {
        Twitter twitter = new TwitterTemplate(accessToken);
        TwitterProfile profile = null;
        try{
            profile = twitter.userOperations().getUserProfile();
        }catch (Exception ex){
            return null;
        }

        List<String> authorities=new ArrayList<String>();
        authorities.add(Authorities.ROLE_ACCESS.name());

        User user =userDao.findByUsername(""+profile.getId());
        if(user == null){
            user = User.newBuilder()
                    .username(""+profile.getId())
                    .authorities(authorities)
                    .password(passwordEncoder.encode(USER_SOCIAL_PASSWORD))
                    .firstname(profile.getName())
                    .lastname("")
                    .socialEnum(SocialEnum.Twitter)
                    .gender(GenderEnum.Others)
                    .displayName(profile.getScreenName())
                    .isSignUpConfirmed(true)
                    .profileImageUrl(profile.getProfileImageUrl())
                    .enabled(true)
                    .build();

            user = userDao.save(user);
        }else{
            user.setFirstname(profile.getName());
            user.setLastname("");
            user.setGender(GenderEnum.Others);
            user.setDisplayName(profile.getName());
            user.setProfileImageUrl(profile.getProfileImageUrl());
            user = userDao.save(user);
        }

        return null;
    }
}
