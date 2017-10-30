package it.italian.coders.model.social;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SocialEnum {
    None(0), Facebook(1), Gmail(2);

    private Integer value;

    @JsonValue
    public  Integer getValue () {
        return value;
    }

    SocialEnum(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static SocialEnum fromValue(String text) {
        for (SocialEnum b : SocialEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
