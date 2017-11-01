package it.italian.coders.model.social;

public final class SocialUtils {
    private final static String PROFILE_PICTURE_SCHEMA = "http://graph.facebook.com/{0}/picture?type=square";
    public final static String USER_SOCIAL_PASSWORD = "*";
    public final static String SOCIAL_MALE_SEX="male";
    public final static String SOCIAL_FEMALE_SEX="female";


    public static String getFacebookProfileImageUrl(String userId){
        return PROFILE_PICTURE_SCHEMA.replace("{0}",userId);
    }
}
