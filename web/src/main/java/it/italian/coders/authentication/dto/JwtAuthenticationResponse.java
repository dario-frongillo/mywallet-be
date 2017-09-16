package it.italian.coders.authentication.dto;



import it.italian.coders.model.authentication.User;

import java.io.Serializable;
import java.util.List;



public class JwtAuthenticationResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;
    private User user;
    private String workstation;

    public JwtAuthenticationResponse(String token, User user, List<String> permissions, String workstation) {
        this.token = token;
        this.user=user;
        this.workstation = workstation;
    }

    public String getToken() {
        return this.token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }
}
