package it.italian.coders.service.social.google.impl;

import it.italian.coders.dao.authentication.UserDao;
import it.italian.coders.model.authentication.Authorities;
import it.italian.coders.model.authentication.GenderEnum;
import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;
import it.italian.coders.service.social.google.GoogleSocialManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static it.italian.coders.model.social.SocialUtils.USER_SOCIAL_PASSWORD;

@Service
public class GoogleSocialManagerImpl implements GoogleSocialManager{

    @Autowired
    UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User updInsSocialUser(String accessToken) {
        Google google = new GoogleTemplate(accessToken);
        Person profile = null;
        try {
            profile = google.plusOperations().getGoogleProfile();
        }catch (Exception e){
            return null;
        }
        List<String> authorities=new ArrayList<String>();
        authorities.add(Authorities.ROLE_ACCESS.name());
        User user =userDao.findByUsername(profile.getId());
        if(user == null){
            user = User.newBuilder()
                    .username(profile.getId())
                    .authorities(authorities)
                    .email(profile.getEmails() == null || profile.getEmails().isEmpty() ? null :profile.getEmails().get(0))
                    .socialEnum(SocialEnum.Google)
                    .password(passwordEncoder.encode(USER_SOCIAL_PASSWORD))
                    .firstname(profile.getGivenName())
                    .lastname(profile.getFamilyName())
                    .isSignUpConfirmed(true)
                    .gender(profile.getGender()==null ? null : GenderEnum.fromSocialValue(profile.getGender()))
                    .displayName(profile.getDisplayName())
                    .profileImageUrl(profile.getImageUrl())
                    .enabled(true)
                    .build();

            user = userDao.save(user);
        }else{
            user.setFirstname(profile.getGivenName());
            user.setLastname(profile.getFamilyName());
            user.setEmail(profile.getEmails() == null || profile.getEmails().isEmpty() ? null :profile.getEmails().get(0));
            user.setGender(profile.getGender()==null ? null : GenderEnum.fromSocialValue(profile.getGender()));
            user.setDisplayName(profile.getDisplayName());
            user.setProfileImageUrl(profile.getImageUrl());
            user = userDao.save(user);
        }

        return user;
    }
}
