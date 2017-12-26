package it.italian.coders.authentication.dto;



import it.italian.coders.model.authentication.User;

import java.io.Serializable;
import java.util.List;



public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private User user;

    public JwtAuthenticationResponse( User user) {
        this.user=user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
