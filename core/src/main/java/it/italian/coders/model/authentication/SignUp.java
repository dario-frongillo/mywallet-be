package it.italian.coders.model.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder(builderMethodName = "newBuilder")
public class SignUp {

    @Size(min = 4, max = 100)
    @NotNull
    private String username;

    @JsonIgnore
    @NotNull
    private String password;

    @NotNull
    @Email
    private String email;


    private String firstname;


    private String lastname;

    private String displayName;


    private GenderEnum gender;

    private String profileImageUrl;
}
