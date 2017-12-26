package it.italian.coders.service.social.facebook.impl;

import it.italian.coders.dao.authentication.UserDao;
import it.italian.coders.model.authentication.Authorities;
import it.italian.coders.model.authentication.GenderEnum;
import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;
import it.italian.coders.model.social.SocialUtils;
import it.italian.coders.service.social.facebook.FacebookSocialManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static it.italian.coders.model.social.SocialUtils.USER_SOCIAL_PASSWORD;

@Service
public class FacebookSocialManagerImpl implements FacebookSocialManager {

    @Value("${spring.social.facebook.app-namespace}")
    private String appNameSpace;

    private final static Logger logger = LoggerFactory.getLogger(FacebookSocialManagerImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        try {
            String[] fieldsToMap = { "id", "about", "age_range", "birthday",
                    "context", "cover", "currency", "devices", "education",
                    "email", "favorite_athletes", "favorite_teams",
                    "first_name", "gender", "hometown", "inspirational_people",
                    "installed", "install_type", "is_verified", "languages",
                    "last_name", "link", "locale", "location", "meeting_for",
                    "middle_name", "name", "name_format", "political",
                    "quotes", "payment_pricepoints", "relationship_status",
                    "religion", "security_settings", "significant_other",
                    "sports", "test_group", "timezone", "third_party_id",
                    "updated_time", "verified", "viewer_can_send_gift",
                    "website", "work" };

            Field field = Class.forName(
                    "org.springframework.social.facebook.api.UserOperations")
                    .getDeclaredField("PROFILE_FIELDS");
            field.setAccessible(true);

            Field modifiers = field.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, fieldsToMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public User updInsSocialUser(String accessToken) {
        Facebook facebook = new FacebookTemplate(accessToken, appNameSpace);
        org.springframework.social.facebook.api.User profile = null;

        try{
            profile = facebook.userOperations().getUserProfile();
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
                    .password(passwordEncoder.encode(USER_SOCIAL_PASSWORD))
                    .firstname(profile.getFirstName())
                    .socialType(SocialEnum.Facebook)
                    .lastname(profile.getLastName())
                    .gender(profile.getGender()==null ? null : GenderEnum.fromSocialValue(profile.getGender()))
                    .displayName(profile.getName())
                    .isSignUpConfirmed(true)
                    .profileImageUrl(SocialUtils.getFacebookProfileImageUrl(profile.getId()))
                    .enabled(true)
                    .build();

            user = userDao.save(user);
        }else{
            user.setFirstname(profile.getFirstName());
            user.setLastname(profile.getLastName());
            user.setGender(profile.getGender()==null ? null : GenderEnum.fromSocialValue(profile.getGender()));
            user.setDisplayName(profile.getName());
            user.setProfileImageUrl(SocialUtils.getFacebookProfileImageUrl(profile.getId()));
            user = userDao.save(user);
        }

        return user;
    }
}
