package it.italian.coders.model.authentication;


import com.fasterxml.jackson.annotation.JsonIgnore;
import it.italian.coders.model.BaseDocument;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Document
public class User extends BaseDocument {

    @Id
    @Size(min = 4, max = 30)
    @NotNull
    private String username;


    @JsonIgnore
    private String password;

    @Email
    @Indexed
    private String email;

    private String firstname;

    private String lastname;

    private String fullname;


    private boolean enabled=false;

    private List<String> authorities;

    private String gender;

    private String profileImageUrl;

    @Builder(builderMethodName = "newBuilder")
    public User(Long version, Date created, Date updated, String username, String password, String email, String firstname, String lastname, boolean enabled, List<String> authorities, String gender,String fullname, String profileImageUrl) {
        super(version, created, updated);
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.enabled = enabled;
        this.authorities = authorities;
        this.gender = gender;
        this.fullname =fullname;
        this.profileImageUrl = profileImageUrl;
    }

}
