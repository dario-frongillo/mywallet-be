package it.italian.coders.model.authentication;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import it.italian.coders.model.social.SocialEnum;
import it.italian.coders.model.social.SocialUtils;

public enum  GenderEnum {
    Male(0), Female(1), Others(2);

    private Integer value;

    @JsonValue
    public  Integer getValue () {
        return value;
    }

    GenderEnum(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static GenderEnum fromValue(String text) {
        for (GenderEnum b : GenderEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    public static GenderEnum fromSocialValue(String text) {
        GenderEnum retVal;
        switch (text){
            case SocialUtils.SOCIAL_MALE_SEX:
                retVal = Male;
                break;
            case SocialUtils.SOCIAL_FEMALE_SEX:
                retVal = Female;
                break;
            default:
                retVal = Others;
        }

        return retVal;

    }

}
