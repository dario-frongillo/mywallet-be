package it.italian.coders.model.verification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VerificationTypeEnum {
    Signup(0), ResetPassword(1);

    private Integer value;

    @JsonValue
    public  Integer getValue () {
        return value;
    }

    VerificationTypeEnum(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static VerificationTypeEnum fromValue(String text) {
        for (VerificationTypeEnum b : VerificationTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
