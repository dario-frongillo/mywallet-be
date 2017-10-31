package it.italian.coders.service.authentication;

import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;

public interface UserManager {
    User findByUsernameIgnoreCase(String username);
    User save (User user);
    User findByUsername(String username);
    User findByEmail(String email);

}
