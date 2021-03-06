package it.italian.coders.authentication.jwt;


/**
 * Created by dario on 09/07/2017.
 */

import it.italian.coders.model.social.SocialEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class  JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String username;
    private String password;
    private SocialEnum socialAuthentication;
    private String socialAccessToken;



}
