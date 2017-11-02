package it.italian.coders.model.verification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VerificationStatusEnum {
    WAITING(0), CLOSED(1), LOCKED(1);

    private Integer value;

    @JsonValue
    public  Integer getValue () {
        return value;
    }

    VerificationStatusEnum(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static VerificationStatusEnum fromValue(String text) {
        for (VerificationStatusEnum b : VerificationStatusEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
